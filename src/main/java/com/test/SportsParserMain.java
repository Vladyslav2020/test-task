package com.test;

import com.test.entities.*;
import com.test.fetchers.DataFetcher;
import com.test.parsers.DataParser;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class SportsParserMain {
    private static final int MAX_THREADS = 3;
    private static DataParser dataParser;
    private static DataFetcher dataFetcher;
    private static final String[] sportNames = new String[]{"Football", "Tennis", "Ice Hockey", "Basketball"};
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");


    public static void main(String[] args) {
        dataParser = new DataParser();
        dataFetcher = new DataFetcher();
        CompletableFuture<List<Sport>> sportsFuture = dataFetcher.fetchSportsData().thenApply(dataParser::parseSportsData);
        sportsFuture.thenAccept(sports -> {
            List<Sport> filteredSports = filterSportsByName(sports, sportNames);
            ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);


            List<CompletableFuture<Game>> futures = fetchGameDataInPool(filteredSports, executorService);

            CompletableFuture<List<Game>> allGamesFuture = waitForFetchCompletion(futures);

            try {
                allGamesFuture.get();
                printResults(filteredSports);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).join();
    }

    private static CompletableFuture<List<Game>> waitForFetchCompletion(List<CompletableFuture<Game>> futures) {
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }

    private static List<CompletableFuture<Game>> fetchGameDataInPool(List<Sport> filteredSports, ExecutorService executorService) {
        List<CompletableFuture<Game>> futures = new ArrayList<>();
        for (Sport sport : filteredSports) {
            for (League league : sport.getLeagues()) {
                CompletableFuture<Game> future = CompletableFuture.supplyAsync(() -> {
                    String gameData = dataFetcher.fetchGameData(league);
                    Game game = dataParser.parseGameData(gameData);
                    String marketData = dataFetcher.fetchMarketData(game);
                    List<Market> markets = dataParser.parseMarketData(marketData);
                    game.setMarkets(markets);
                    league.setGame(game);
                    return game;
                }, executorService);

                futures.add(future);
            }
        }
        return futures;
    }

    private static void printResults(List<Sport> filteredSports) {
        for (Sport sport : filteredSports) {
            System.out.println("Sport: " + sport.getName());
            List<League> leagues = sport.getLeagues();
            for (League league : leagues) {
                Game game = league.getGame();
                System.out.println("   League: " + league.getName());
                System.out.println("        Game name: " + game.getName());
                System.out.println("        Kickoff (UTC): " + game.getStartTime().format(formatter));
                System.out.println("        Game ID: " + game.getId());
                for (Market market : game.getMarkets()) {
                    System.out.println("	           Market name: " + market.getName());
                    for (Runner runner : market.getRunners()) {
                        System.out.println("	               " + runner.getName() + ", " + runner.getPriceStr() + ", " + runner.getId());
                    }
                }
                System.out.println();
            }
        }
    }


    public static List<Sport> filterSportsByName(List<Sport> sports, String... sportNames) {
        return sports.stream()
                .filter(sport -> containsIgnoreCase(sport.getName(), sportNames))
                .collect(Collectors.toList());
    }

    public static boolean containsIgnoreCase(String input, String... values) {
        for (String value : values) {
            if (input.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}

