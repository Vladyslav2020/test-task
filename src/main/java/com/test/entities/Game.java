package com.test.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private long id;
    private String name;
    private LocalDateTime startTime;

    private List<Market> markets;


    public Game(long id, String name, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.markets = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }
}
