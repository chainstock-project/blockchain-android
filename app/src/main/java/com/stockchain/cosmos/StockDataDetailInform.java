package com.stockchain.cosmos;

import java.util.ArrayList;

public class StockDataDetailInform {
    String date;

    String code;
    String name;
    String market_type;
    int dayOverDayAmount;
    Double dayOverDayRate;


    int previousCloseAmount;
    int numberOfStock;

    long marketCap;
    long marketCapRank;
    int parValue;
    int tradingUnit;

    int stock52WeekHight;
    int stock52WeekLow;

    float PER;
    float EPS;
    float PBR;
    float BPS;
    float sameInderstryPER;

    ArrayList<MarketPrice> marketPriceList = new ArrayList<>();
    class MarketPrice{
        String date;
        int amount;
    }
}