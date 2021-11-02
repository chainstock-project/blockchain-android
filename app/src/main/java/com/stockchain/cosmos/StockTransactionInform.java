package com.stockchain.cosmos;

public class StockTransactionInform implements Comparable<StockTransactionInform> {
    String code;
    int count;
    long purchaseAmount;

    String name;
    long currentAmount;
    double earningPrice;

    StockTransactionInform(String code, int count, int purchase_amount){
        this.code = code;
        this.count = count;
        this.purchaseAmount = purchase_amount;
    }

    @Override
    public int compareTo(StockTransactionInform stockTransactionInform) {
        Long a = new Long(this.currentAmount);
        return a.compareTo(stockTransactionInform.currentAmount);
    }
}