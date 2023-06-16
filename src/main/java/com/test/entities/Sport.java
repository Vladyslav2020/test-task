package com.test.entities;

import java.util.List;

public class Sport {
    private String name;
    private List<League> leagues;

    public Sport(String name, List<League> leagues) {
        this.name = name;
        this.leagues = leagues;
    }

    public String getName() {
        return name;
    }

    public List<League> getLeagues() {
        return leagues;
    }
}
