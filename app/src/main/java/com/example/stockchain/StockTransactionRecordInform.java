package com.example.stockchain;

public class StockTransactionRecordInform {
    String code;
    long amount;
    long count;
    String date;
    String recordType;

    StockTransactionRecordInform(String code, long amount, long count, String date, String recordType){
        this.code = code;
        this.amount = amount;
        this.count = count;
        this.date = date;
        this.recordType = recordType;
    }
}
