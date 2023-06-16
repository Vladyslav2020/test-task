package com.test.parsers;

import com.test.entities.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public List<Sport> parseSportsData(String responseBody) {
        List<Sport> sports = new ArrayList<>();

        try {
            JSONArray sportsArray = new JSONArray(responseBody);
            for (int i = 0; i < sportsArray.length(); i++) {
                JSONObject sportObject = sportsArray.getJSONObject(i);

                String name = sportObject.getString("name");
                JSONArray regionsArray = sportObject.getJSONArray("regions");
                List<League> leagues = new ArrayList<>();

                for (int j = 0; j < regionsArray.length(); j++) {
                    JSONObject regionObject = regionsArray.getJSONObject(j);

                    JSONArray leaguesArray = regionObject.getJSONArray("leagues");

                    for (int k = 0; k < leaguesArray.length(); k++) {
                        JSONObject leagueObject = leaguesArray.getJSONObject(k);

                        boolean top = leagueObject.getBoolean("top");
                        if (!top) {
                            continue;
                        }
                        long id = leagueObject.getLong("id");
                        String leagueName = leagueObject.getString("name");

                        League league = new League(id, leagueName);
                        leagues.add(league);
                    }
                }

                Sport sport = new Sport(name, leagues);
                sports.add(sport);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sports;
    }

    public Game parseGameData(String responseBody) {
        try {
            JSONObject responseObj = new JSONObject(responseBody);
            JSONArray dataArray = responseObj.getJSONArray("data");

            if (dataArray.length() > 0) {
                JSONObject gameObj = dataArray.getJSONObject(0);
                long id = gameObj.getLong("id");
                String name = gameObj.getString("name");
                long kickoff = gameObj.getLong("kickoff");

                return new Game(id, name, LocalDateTime.ofEpochSecond(kickoff / 1000, 0, ZoneOffset.UTC));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Market> parseMarketData(String marketData) {
        try {
            List<Market> markets = new ArrayList<>();
            JSONObject responseJson = new JSONObject(marketData);
            JSONArray marketsJson = responseJson.getJSONArray("markets");

            for (int i = 0; i < marketsJson.length(); i++) {
                JSONObject marketJson = marketsJson.getJSONObject(i);
                String marketName = marketJson.getString("name");
                JSONArray runnersJson = marketJson.getJSONArray("runners");

                Market market = new Market(marketName);

                for (int j = 0; j < runnersJson.length(); j++) {
                    JSONObject runnerJson = runnersJson.getJSONObject(j);
                    String runnerName = runnerJson.getString("name");
                    String price = runnerJson.getString("priceStr");
                    long runnerId = runnerJson.getLong("id");

                    Runner runner = new Runner(runnerName, price, runnerId);
                    market.addRunner(runner);
                }

                markets.add(market);
            }

            return markets;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
