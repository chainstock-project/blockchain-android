package com.stockchain.cosmos;

import java.util.ArrayList;

public class StockDataDetailInform {
    String code;
    String name;
    String market_type;
    String date;

    int previousCloseAmount;
    int todayCloseAmount;
    int dayOverDayAmount;
    Double dayOverDayRate;
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