package com.stockchain.cosmos;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class StockTransaction {
    private Context ctx;
    private String blockchainPath;
    private String homeDir;

    public StockTransaction(Context ctx) {
        this.ctx = ctx;
        this.blockchainPath = ctx.getApplicationInfo().nativeLibraryDir + "/blockchaind.so";
        this.homeDir = ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";
    }

    public String createStockTransaction(String username, String code, int count) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "create-stock-transaction", code, String.valueOf(count), "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "--gas=auto", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }else{
            String tx="";
            while(line != null){
                tx = line;
                line = stdOut.readLine();
            }
            return tx.substring(8);
        }
    }

    public String deleteStockTransaction(String username, String code, int count)throws IOException{
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "delete-stock-transaction", code, String.valueOf(count), "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }
        else{
            String tx="";
            while(line != null){
                tx = line;
                line = stdOut.readLine();
            }
            return tx.substring(8);
        }
    }

    private ArrayList<StockTransactionInform> getStockTransactionList(String address) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "show-stock-transaction", address, "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();

        ArrayList<StockTransactionInform> StockTransactionInformList = new ArrayList<StockTransactionInform>();
        if (line == null) {
            return StockTransactionInformList;
        }else {
            stdOut.readLine(); stdOut.readLine();
            while ((line = stdOut.readLine()) != null) {
                String[] line_split = line.split(" ");
                String code = line_split[line_split.length - 1].replace("\"", "");

                line = stdOut.readLine();
                line_split = line.split(" ");
                int count = (int) Double.parseDouble(line_split[line_split.length - 1]);

                line = stdOut.readLine();
                line_split = line.split(" ");
                int puerchase_amount = (int) Double.parseDouble(line_split[line_split.length - 1]);

                StockTransactionInform StockTransaction = new StockTransactionInform(code, count, puerchase_amount);
                StockTransactionInformList.add(StockTransaction);
            }

            return StockTransactionInformList;
        }
    }

    public ArrayList<StockTransactionInform> getStockTransactionInformList(String address) throws IOException {
        StockData stockData = new StockData(this.ctx);
        ArrayList<StockTransactionInform> StockTransactionInformList = this.getStockTransactionList(address);

        for(int i=0;i<StockTransactionInformList.size();i++){
            StockTransactionInform stockTransactionInform = StockTransactionInformList.get(i);
            StockDataInform stockDatainform = stockData.getStockData(stockTransactionInform.code);

            stockTransactionInform.name = stockDatainform.name;
            stockTransactionInform.currentAmount = stockDatainform.amount * stockTransactionInform.count;
            stockTransactionInform.earningPrice = (((double)stockTransactionInform.currentAmount / stockTransactionInform.purchaseAmount) - 1) * 100;
        }
        return StockTransactionInformList;
    }

    static public StockTransactionInform searchStockTransactionInform(ArrayList<StockTransactionInform> list, String code){
        for(int i=0;i<list.size();i++){
            if(list.get(i).code.equals(code)){
                return list.get(i);
            }
        }
        return null;
    }

    static public int getNumberOfStock(ArrayList<StockTransactionInform> list, String code){
        StockTransactionInform stockTransactionInform = searchStockTransactionInform(list, code);
        if(stockTransactionInform == null) {
            return 0;
        }
        else{
            return stockTransactionInform.getCount();
        }
    }

    public boolean checkStockTransactionCreated(String address, String code, int afteruNumberOfStock){
        try {
            ArrayList<StockTransactionInform> stockTransactionList = getStockTransactionList(address);
            int numberOfStock = getNumberOfStock(stockTransactionList,code);
            while(numberOfStock != afteruNumberOfStock){
                stockTransactionList = getStockTransactionList(address);
                numberOfStock = getNumberOfStock(stockTransactionList,code);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private int getCurrentStockTransactionTotalAmount(ArrayList<StockTransactionInform> stockTransactionInformList) throws IOException{
        int amount = 0;
        for(StockTransactionInform h : stockTransactionInformList){
            amount += h.currentAmount;
        }
        return amount;
    }

    private long getBalance(String address) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "bank", "balances", address, "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }

        line = stdOut.readLine();
        String[] line_split = line.split(" ");
        String amount = line_split[line_split.length - 1].replace("\"", "");
        return Long.parseLong(amount);
    }

    public StockBankInform getStockBankInform(ArrayList<StockTransactionInform> stockTransactionInformList, String address) throws IOException {
        long balances = this.getBalance(address);
        long currentStockTotalAmount = this.getCurrentStockTransactionTotalAmount(stockTransactionInformList);
        long currentTotalAmount = balances + currentStockTotalAmount;
        double earning_rate = (((double) currentTotalAmount / 1000000) - 1) * 100;

        StockBankInform stockBankInform = new StockBankInform(currentTotalAmount, balances, currentStockTotalAmount, earning_rate);
        return stockBankInform;
    }

    public ArrayList<StockTransactionRecordInform> getStockTransactionRecord(String address) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "show-stock-transaction-record", address, "--home", homeDir);
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }else {
            stdOut.readLine(); stdOut.readLine();

            ArrayList<StockTransactionRecordInform> stockTransactionRecordInformList = new ArrayList<>();
            StockData stockData = new StockData(ctx);
            while ((line = stdOut.readLine()) != null) {
                String[] line_split = line.split(" ");
                long amount = Long.parseLong(line_split[line_split.length - 1]);

                line = stdOut.readLine();
                line_split = line.split(" ");
                String code = line_split[line_split.length - 1].replace("\"", "");

                StockDataInform stockDataInform = stockData.getStockData(code);
                String name = stockDataInform.getName();

                line = stdOut.readLine();
                line_split = line.split(" ");
                int count = (int) Double.parseDouble(line_split[line_split.length - 1]);

                line = stdOut.readLine();
                line_split = line.split(" ");
                String date = line_split[line_split.length - 1].replace("\"", "");

                line = stdOut.readLine();
                line_split = line.split(" ");
                String recordType = line_split[line_split.length - 1].replace("\"", "");

                StockTransactionRecordInform stockTransactionRecordInform = new StockTransactionRecordInform(code, name, amount, count, date, recordType);
                stockTransactionRecordInformList.add(stockTransactionRecordInform);
            }
            Collections.reverse(stockTransactionRecordInformList);
            return stockTransactionRecordInformList;
        }
    }

    public void addStockTransactionRecordDate(ArrayList<StockTransactionRecordInform> list){
        StockTransactionRecordInform record = new StockTransactionRecordInform(list.get(0).date, "DATE");
        list.add(0,record);
        for(int i=0;i<list.size();i++){
            if(!record.date.equals(list.get(i).date)){
                record = new StockTransactionRecordInform(list.get(i).date, "DATE");
                list.add(i,record);
            }
        }
    }

    public ArrayList<StockTransactionInform> getStockTransactionTop3(ArrayList<StockTransactionInform> stockTransactionInformList) throws IOException {
        Collections.sort(stockTransactionInformList);
        return stockTransactionInformList;
    }


}
