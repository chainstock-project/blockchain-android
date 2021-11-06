package com.stockchain.cosmos;

import java.util.ArrayList;

public class StockDataDetailInform {
    String date;

    String code;
    String name;
    String market_type;
    String dayOverDayAmount;
    String dayOverDayRate;

    String previousCloseAmount;
    String numberOfStock;

    String marketSum;
    String marketSumRanking;
    String faceValue;
    String tradingUnit;

    String PER;
    String EPS;
    String PBR;
    String BPS;
    String sameInderstryPER;

    public StockDataDetailInform(String date, String code, String name, String market_type, String dayOverDayAmount, String dayOverDayRate, String previousCloseAmount, String numberOfStock, String marketSum, String marketSumRanking, String faceValue, String tradingUnit, String PER, String EPS, String PBR, String BPS, String sameInderstryPER) {
        this.date = date;
        this.code = code;
        this.name = name;
        this.market_type = market_type;
        this.dayOverDayAmount = dayOverDayAmount;
        this.dayOverDayRate = dayOverDayRate;
        this.previousCloseAmount = previousCloseAmount;
        this.numberOfStock = numberOfStock;
        this.marketSum = marketSum;
        this.marketSumRanking = marketSumRanking;
        this.faceValue = faceValue;
        this.tradingUnit = tradingUnit;
        this.PER = PER;
        this.EPS = EPS;
        this.PBR = PBR;
        this.BPS = BPS;
        this.sameInderstryPER = sameInderstryPER;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getDayOverDayAmount() {
        return dayOverDayAmount;
    }

    public void setDayOverDayAmount(String dayOverDayAmount) {
        this.dayOverDayAmount = dayOverDayAmount;
    }

    public String getDayOverDayRate() {
        return dayOverDayRate;
    }

    public void setDayOverDayRate(String dayOverDayRate) {
        this.dayOverDayRate = dayOverDayRate;
    }

    public String getPreviousCloseAmount() {
        return previousCloseAmount;
    }

    public void setPreviousCloseAmount(String previousCloseAmount) {
        this.previousCloseAmount = previousCloseAmount;
    }

    public String getNumberOfStock() {
        return numberOfStock;
    }

    public void setNumberOfStock(String numberOfStock) {
        this.numberOfStock = numberOfStock;
    }

    public String getMarketSum() {
        return marketSum;
    }

    public void setMarketSum(String marketSum) {
        this.marketSum = marketSum;
    }

    public String getMarketSumRanking() {
        return marketSumRanking;
    }

    public void setMarketSumRanking(String marketSumRanking) {
        this.marketSumRanking = marketSumRanking;
    }

    public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }

    public String getTradingUnit() {
        return tradingUnit;
    }

    public void setTradingUnit(String tradingUnit) {
        this.tradingUnit = tradingUnit;
    }

    public String getPER() {
        return PER;
    }

    public void setPER(String PER) {
        this.PER = PER;
    }

    public String getEPS() {
        return EPS;
    }

    public void setEPS(String EPS) {
        this.EPS = EPS;
    }

    public String getPBR() {
        return PBR;
    }

    public void setPBR(String PBR) {
        this.PBR = PBR;
    }

    public String getBPS() {
        return BPS;
    }

    public void setBPS(String BPS) {
        this.BPS = BPS;
    }

    public String getSameInderstryPER() {
        return sameInderstryPER;
    }

    public void setSameInderstryPER(String sameInderstryPER) {
        this.sameInderstryPER = sameInderstryPER;
    }
}