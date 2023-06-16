package com.test.entities;

public class League {
    private long id;
    private String name;
    private Game game;

    public League(long id, String leagueName) {
        this.id = id;
        this.name = leagueName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
