package com.example.stockchain;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StockData {
    private final String blockchainPath;
    private final String homeDir;

    public StockData(Context ctx){
        this.blockchainPath = ctx.getApplicationInfo().nativeLibraryDir + "/blockchaind.so";
        this.homeDir = ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";
    }

    public ArrayList<StockDataInform> getStockDataList() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "list-stock-data", "--limit", "5000", "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null || line.equals("StockData: []")) {
            throw new IOException("dosen't exists");
        }

        ArrayList<StockDataInform> stockDataInformList = new ArrayList<>(); // Create an ArrayList object
        while ((line = stdOut.readLine()) != null) {
            if(line.equals("pagination:")){
                break;
            }

            String[] line_split = line.split(" ");
            int amount = (int) Double.parseDouble(line_split[line_split.length - 1]);

            line = stdOut.readLine();
            line_split = line.split(" ");
            String get_code = line_split[line_split.length - 1].replace("\"", "");

            line = stdOut.readLine();
            line_split = line.split(" ");
            String creator = line_split[line_split.length - 1];

            line = stdOut.readLine();
            line_split = line.split(" ");
            String date = line_split[line_split.length - 1];

            line = stdOut.readLine();
            line_split = line.split(" ");
            String market_type = line_split[line_split.length - 1];

            line = stdOut.readLine();
            line_split = line.split(" ");
            String name = line_split[line_split.length - 1];

            StockDataInform stockDataInform = new StockDataInform(get_code, name, market_type, amount, date);
            stockDataInformList.add(stockDataInform);
        }

        return stockDataInformList;
    }


    public StockDataInform getStockData(String code) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "show-stock-data", code, "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line=stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }

        String[] line_split;
        line = stdOut.readLine();
        line_split = line.split(" ");
        int amount = (int) Double.parseDouble(line_split[line_split.length - 1]);

        line = stdOut.readLine();
        line_split = line.split(" ");
        String get_code = line_split[line_split.length - 1].replace("\"", "");

        line = stdOut.readLine();
        line_split = line.split(" ");
        String creator = line_split[line_split.length - 1];

        line = stdOut.readLine();
        line_split = line.split(" ");
        String date = line_split[line_split.length - 1];

        line = stdOut.readLine();
        line_split = line.split(" ");
        String market_type = line_split[line_split.length - 1];

        line = stdOut.readLine();
        line_split = line.split(" ");
        String name = line_split[line_split.length - 1];

        StockDataInform stockDataInform = new StockDataInform(get_code, name, market_type, amount, date);
        return stockDataInform;
    }

}
