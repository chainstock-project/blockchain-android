package com.stockchain.cosmos;

public class StockBankInform {
    long currentTotalAmount;
    long balances;
    long currentStockTotalAmount;
    double earningRate;

    public StockBankInform(long currentTotalAmount, long balances, long currentStockTotalAmount, double earningRate){
        this.currentTotalAmount = currentTotalAmount;
        this.balances = balances;
        this.currentStockTotalAmount = currentStockTotalAmount;
        this.earningRate = earningRate;
    }
}
