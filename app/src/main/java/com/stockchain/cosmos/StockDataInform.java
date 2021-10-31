package com.stockchain.cosmos;

public class StockDataInform {
    String code;
    String name;
    String market_type;
    int amount;
    String date;

    StockDataInform(String code, String name, String market_type, int amount, String date){
        this.code = code;
        this.name = name;
        this.market_type = market_type;
        this.amount = amount;
        this.date = date;
    }
}
