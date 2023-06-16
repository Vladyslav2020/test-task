package com.test.entities;

import java.util.ArrayList;
import java.util.List;

public class Market {
    private String name;
    private List<Runner> runners;

    public Market(String name) {
        this.name = name;
        this.runners = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Runner> getRunners() {
        return runners;
    }

    public void addRunner(Runner runner) {
        runners.add(runner);
    }
}
