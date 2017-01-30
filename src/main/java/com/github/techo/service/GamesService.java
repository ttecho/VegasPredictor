package com.github.techo.service;

import com.github.techo.entities.Game;
import com.github.techo.entities.GameResult;
import com.github.techo.entities.Vegas;
import com.github.techo.entities.VegasProjection;
import com.github.techo.entities.VegasResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GamesService {
    private static final String VEGAS_INSIDER_DOMAIN = "http://www.vegasinsider.com/";

    @Autowired
    private VegasService vegasService;

    public List<Game> retrieveGameResults(String teamResultsUrl, String originalTeam, boolean retrieveOpponentsSchedule) {
        return getGames(teamResultsUrl, originalTeam, retrieveOpponentsSchedule)
                .parallelStream()
                .map(game -> {
                    if (retrieveOpponentsSchedule) {
                        System.out.println("RETRIEVING " + game.getOpponentName() + " RESULTS...");
                        game.setOpponentGames(retrieveGameResults(game.getOpponentUrl(), game.getOpponentName(), false));
                        System.out.println("FINISHED RETRIEVING " + game.getOpponentName() + " RESULTS!");
                    }
                    return game;
                })
                .collect(Collectors.toList());
    }

    List<Game> getPlayedGames(String teamUrl, String teamName) {
        return retrieveGameResults(teamUrl, teamName, false)
                .parallelStream()
                .filter(game -> game.getGameResult() != null)
                .collect(Collectors.toList());
    }

    private List<Game> getGames(String teamResultsUrl, String originalTeam, boolean selectedTeamSearch) {
        URLConnection conn;
        try {
            conn = openConnection(teamResultsUrl);

            Document doc = getDocumentFromConnection(conn);

            Elements gamesPlayed = doc
                    .select("body")
                    .get(0)
                    .select("table tbody:contains(TEAM SCHEDULE & RESULTS)")
                    .get(1)
                    .select("tbody")
                    .get(1)
                    .select("tr");

            gamesPlayed.remove(0);
            return collectGames(gamesPlayed, originalTeam, selectedTeamSearch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<Game> collectGames(Elements gamesPlayed, String originalTeam, boolean selectedTeamSearch) {
        return gamesPlayed
                .parallelStream()
                .map(element -> convertToGame(element, originalTeam, selectedTeamSearch))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Game convertToGame(Element gameElement, String originalTeamName, boolean selectedTeamSearch) {
        Game game = new Game();
        game.setTeamName(originalTeamName);
        GameResult gameResult = new GameResult();
        try {
            List<Node> children = gameElement.childNodes();

            List<Node> dataChildren = IntStream.range(1, children.size())
                    .filter(n -> n % 2 == 1)
                    .mapToObj(children::get)
                    .collect(Collectors.toList());

            Node date = dataChildren.get(0);
            Node opponent = dataChildren.get(1);
            Node line = dataChildren.get(2);
            Node total = dataChildren.get(3);
            Node result = dataChildren.get(4);

            String[] resultValues = result.childNode(0).toString().split(" ");

            game.setDate(getDate(date));
            game.setHomeTeam(isHomeTeam(opponent));
            game.setOpponentName(getOpposingTeamName(opponent));
            game.setOpponentUrl(getOpposingTeamUrl(opponent));

            VegasProjection vegasProjection = vegasService
                    .calculateVegasProjectedScore(getLine(line), getTotalForOverUnder(total));

            gameResult.setOpponentPoints(getOpposingTeamPoints(resultValues));
            gameResult.setPoints(getTeamPoints(resultValues));

            VegasResult vegasResult = vegasService.calculateVegasResult(
                    vegasProjection,
                    gameResult.getPoints(),
                    gameResult.getOpponentPoints());

            Vegas vegas = new Vegas();
            vegas.setProjection(vegasProjection);
            vegas.setResult(vegasResult);

            game.setGameResult(gameResult);
            game.setVegas(vegas);

            System.out.println("Retrieved game between: " + originalTeamName + " vs. " + game.getOpponentName());
        } catch (Exception ignore) {
            //TODO: Handle Exceptions for games without vegas lines
            System.out.println("The game between: " + originalTeamName + " and " + game.getOpponentName() + " did not contain all data and caused an error...");
            if (!selectedTeamSearch && gameResult.getPoints() == 0) {
                System.out.println("This is not the searched team, so we skip the non qualified results.");
                return null;
            }
        }

        return game;
    }

    //TODO: Handle Exceptions
    private String getDate(Node date) {
        return date.childNode(0).toString().trim();
    }

    private boolean isHomeTeam(Node opponent) {
        return !opponent.childNode(0).toString().contains("@");
    }

    private String getOpposingTeamName(Node opponent) {
        return opponent.childNode(1).childNode(0).toString().trim();
    }

    private String getOpposingTeamUrl(Node opponent) {
        return VEGAS_INSIDER_DOMAIN + opponent.childNode(1).attr("href");
    }

    private double getLine(Node line) {
        return Double.parseDouble(line.childNode(0).toString().trim());
    }

    private double getTotalForOverUnder(Node total) {
        return Double.parseDouble(total.childNode(0).toString().trim());
    }

    private int getOpposingTeamPoints(String[] resultValues) {
        return Integer.parseInt(resultValues[4]);
    }

    private int getTeamPoints(String[] resultValues) {
        return Integer.parseInt(resultValues[2]);
    }

    private URLConnection openConnection(String urlToConnect) throws MalformedURLException, IOException {
        try {
            URL url = new URL(urlToConnect);
            URLConnection conn = url.openConnection();
            return conn;
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    private Document getDocumentFromConnection(URLConnection conn) throws MalformedURLException, IOException {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            String html = "";
            while ((inputLine = br.readLine()) != null) {
                html += inputLine;
            }

            return Jsoup.parse(html);
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

}
