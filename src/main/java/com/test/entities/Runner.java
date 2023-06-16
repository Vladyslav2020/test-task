package com.test.entities;

public class Runner {
    private String name;
    private String priceStr;
    private long id;

    public Runner(String name, String priceStr, long id) {
        this.name = name;
        this.priceStr = priceStr;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public long getId() {
        return id;
    }
}
