package com.stockchain.cosmos;

public class StockTransactionRecordInform {
    String code;
    String name;
    long amount;
    long count;
    String date;
    String recordType;

    public StockTransactionRecordInform(String code, String name,long amount, long count, String date, String recordType){
        this.code = code;
        this.name = name;
        this.amount = amount;
        this.count = count;
        this.date = date;
        this.recordType = recordType;
    }

    public StockTransactionRecordInform(String date, String recordType){
        this.date = date;
        this.recordType = recordType;
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
}
