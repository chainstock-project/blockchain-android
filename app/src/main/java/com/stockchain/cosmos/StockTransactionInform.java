package com.stockchain.cosmos;

public class StockTransactionInform implements Comparable<StockTransactionInform> {
    String code;
    int count;
    long purchaseAmount;

    String name;
    String marketType;
    long currentAmount;
    double earningPrice;

    StockTransactionInform(String code, int count, int purchase_amount){
        this.code = code;
        this.count = count;
        this.purchaseAmount = purchase_amount;
    }

    public StockTransactionInform(String code, int count, long purchaseAmount, String name, String marketType, long currentAmount, double earningPrice) {
        this.code = code;
        this.count = count;
        this.purchaseAmount = purchaseAmount;
        this.name = name;
        this.marketType = marketType;
        this.currentAmount = currentAmount;
        this.earningPrice = earningPrice;
    }

    @Override
    public int compareTo(StockTransactionInform stockTransactionInform) {
        Long a = new Long(this.currentAmount);
        return a.compareTo(stockTransactionInform.currentAmount);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(long purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public double getEarningPrice() {
        return earningPrice;
    }

    public void setEarningPrice(double earningPrice) {
        this.earningPrice = earningPrice;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }
}