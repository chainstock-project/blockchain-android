package com.example.stockchain;

public class StockTransactionInform {
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
}
