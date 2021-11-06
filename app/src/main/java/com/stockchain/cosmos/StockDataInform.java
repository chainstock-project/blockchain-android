package com.stockchain.cosmos;

public class StockDataInform {
    String code;
    String name;
    String market_type;
    int amount;
    String date;

    public StockDataInform(String code, String name, String market_type, int amount, String date){
        this.code = code;
        this.name = name;
        this.market_type = market_type;
        this.amount = amount;
        this.date = date;
    }
    public StockDataInform(String code, String name, String market_type, int amount){
        this.code = code;
        this.name = name;
        this.market_type = market_type;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket_type() {
        return market_type;
    }

    public void setMarket_type(String market_type) {
        this.market_type = market_type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

