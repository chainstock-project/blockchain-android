package com.stockchain.cosmos;

public class RankInform {
    int ranking;
    String username;
    int amount;

    public RankInform(int ranking, String name, int amount) {
        this.ranking = ranking;
        this.username = name;
        this.amount = amount;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
