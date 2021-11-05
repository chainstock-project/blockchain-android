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

    public long getCurrentTotalAmount() {
        return currentTotalAmount;
    }

    public void setCurrentTotalAmount(long currentTotalAmount) {
        this.currentTotalAmount = currentTotalAmount;
    }

    public long getBalances() {
        return balances;
    }

    public void setBalances(long balances) {
        this.balances = balances;
    }

    public long getCurrentStockTotalAmount() {
        return currentStockTotalAmount;
    }

    public void setCurrentStockTotalAmount(long currentStockTotalAmount) {
        this.currentStockTotalAmount = currentStockTotalAmount;
    }

    public double getEarningRate() {
        return earningRate;
    }

    public void setEarningRate(double earningRate) {
        this.earningRate = earningRate;
    }
}
