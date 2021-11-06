package com.stockchain.cosmos;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class StockData {
    private Context ctx;
    private final String blockchainPath;
    private final String homeDir;

    public StockData(Context ctx) {
        this.ctx = ctx;
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
            if (line.equals("pagination:")) {
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
            line_split = line.split("name: ");
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
        String line = stdOut.readLine();
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
        line_split = line.split("name: ");
        String name = line_split[line_split.length - 1];

        StockDataInform stockDataInform = new StockDataInform(get_code, name, market_type, amount, date);
        return stockDataInform;
    }

    static public StockDataDetailInform getStockDataDetail(StockDataInform stockDataInform, ArrayList<StockTransactionInform> list){

        class StockDataDetail extends Thread {
            StockDataDetailInform stockDataDetailInform = null;
            public void setStockDataDetail() {
                try {
                    String url = "https://finance.naver.com/item/main.nhn?code="+stockDataInform.code;
                    Document document = Jsoup.connect(url).get();

                    String date = stockDataInform.date;
                    String name = stockDataInform.name;
                    String stockCode = stockDataInform.code;
                    String markType = stockDataInform.market_type;
                    String dayOverDayAmount = String.valueOf(stockDataInform.amount);
//                    textDayOverDayAmount
                    Elements dayOverDayRateElements = document.select("#chart_area > div.rate_info > div > p.no_exday > em:nth-child(4) > span:not(span.blind)");
                    String dayOverDayRate = "";
                    for(int i=0;i<dayOverDayRateElements.size()-1;i++){
                        dayOverDayRate += dayOverDayRateElements.get(i).text();
                    }

                    Elements previousClosePriceElements= document.select("#chart_area > div.rate_info > table > tbody > tr:nth-child(1) > td.first > em > span:not(span.blind)");
                    String previousClosePrice = "";
                    for(int i=0;i<previousClosePriceElements.size();i++){
                        previousClosePrice += previousClosePriceElements.get(i).text();
                    }
                    previousClosePrice = previousClosePrice.replace(",","");

                    String numberOfStock;
                    StockTransactionInform stockTransactionInform = StockTransaction.searchStockTransactionInform(list, stockCode);
                    if(stockTransactionInform == null) {
                        numberOfStock = "0";
                    }
                    else{
                         numberOfStock = String.valueOf(stockTransactionInform.getCount());
                    }

                    String marketSum = "N/A";
                    try{
                        marketSum = document.select("#_market_sum").get(0).text();
                    }catch (Exception e){}
                    String marketRanking = "N/A";
                    try{
                        marketRanking = document.select("#tab_con1 > div.first > table > tbody > tr:nth-child(2) > td > em").get(0).text();
                    }catch (Exception e){}
                    String faceValue = "N/A";
                    try{
                        faceValue = document.select("#tab_con1 > div.first > table > tbody > tr:nth-child(4) > td > em:nth-child(1)").get(0).text();
                    }catch (Exception e){}
                    String tradingUnit = "N/A";
                    try{
                        tradingUnit = document.select("#tab_con1 > div.first > table > tbody > tr:nth-child(4) > td > em:nth-child(3)").get(0).text();
                    }catch (Exception e){}

                    String PER="N/A";
                    try{
                        PER = document.select("#_per").get(0).text();
                    }catch (Exception e){}
                    String EPS="N/A";
                    try{
                        EPS = document.select("#_eps").get(0).text();
                    }catch (Exception e){}
                    String PBR="N/A";
                    try{
                        PBR = document.select("#_pbr").get(0).text();
                    }catch (Exception e){}
                    String BPS="N/A";
                    try{
                        BPS = document.select("#tab_con1 > div:nth-child(5) > table > tbody:nth-child(3) > tr:nth-child(2) > td > em:nth-child(3)").get(0).text();
                    }catch (Exception e){}
                    String sameInderstryPER = "N/A";
                    try {
                        sameInderstryPER = document.select("#tab_con1 > div:nth-child(6) > table > tbody > tr.strong > td > em").get(0).text();
                    }catch (Exception e){}

                    stockDataDetailInform = new StockDataDetailInform(date, stockCode, name, markType, dayOverDayAmount, dayOverDayRate, previousClosePrice, numberOfStock, marketSum, marketRanking, faceValue, tradingUnit, PER,EPS, PBR, BPS, sameInderstryPER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void run(){
                setStockDataDetail();
            }

            public StockDataDetailInform getStockDataDetailInform() {
                return stockDataDetailInform;
            }
        }

        //main
        StockDataDetail stockDataDetail = new StockDataDetail();
        stockDataDetail.start();
        try {
            stockDataDetail.join();
            return stockDataDetail.getStockDataDetailInform();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return stockDataDetail.getStockDataDetailInform();
        }
    }

    static public ArrayList<Entry> getStockPrice(String code) {
        ArrayList<Entry> stockPriceList = new ArrayList<>();

        class StockPrice extends Thread {
            public void setStockPrice(String code) {
                try {
                    int lastPage = 3;
                    for (int page = 1; page <= lastPage; page++) {
                        String url = "https://finance.naver.com/item/sise_day.naver?code=035420&page="+page;
                        Document document = Jsoup.connect(url).get();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");

                        for (int i = 3; i <= 7; i++) {
                            Elements dateElement = document.select("body > table.type2 > tbody > tr:nth-child("+i+") > td:nth-child(1) > span");
                            Elements numberElement = document.select("body > table.type2 > tbody > tr:nth-child("+i+") > td:nth-child(2) > span");
                            StockPrice stockPrice;
                            float date = df.parse(dateElement.get(0).text()).getTime() / 1000;
                            float number = Float.valueOf(numberElement.get(0).text().replace(",", ""));
                            stockPriceList.add(new Entry(date, number));
                        }
                        for(int i=11;i<=15;i++){
                            Elements dateElement = document.select("body > table.type2 > tbody > tr:nth-child("+i+") > td:nth-child(1) > span");
                            Elements numberElement = document.select("body > table.type2 > tbody > tr:nth-child("+i+") > td:nth-child(2) > span");
                            float date = df.parse(dateElement.get(0).text()).getTime() / 1000;
                            float number = Float.valueOf(numberElement.get(0).text().replace(",", ""));
                            stockPriceList.add(new Entry(date, number));
                        }
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }

            public void run(){
                setStockPrice(code);
            }
        }

        //main
        StockPrice stockPrice = new StockPrice();
        stockPrice.start();
        try {
            stockPrice.join();
            Collections.reverse(stockPriceList);
            return stockPriceList;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return stockPriceList;
        }
    }

    static public ArrayList<StockDataInform> searchStock(ArrayList<StockDataInform> stockDataInformList, String name) {
        ArrayList<StockDataInform> searchedStockDataInformList = new ArrayList<>();

        for(int i=0;i<stockDataInformList.size();i++){
            if (stockDataInformList.get(i).name.toLowerCase().contains(name.toLowerCase())) {
                searchedStockDataInformList.add(stockDataInformList.get(i));
            }
        }
        return searchedStockDataInformList;
    }

    static public ArrayList<Entry> getKospiEntryMonth() {
        ArrayList<Entry> values = new ArrayList<>();

        class KospiPrice extends Thread {
            ArrayList<Entry> values = new ArrayList<>();
            public ArrayList<Entry> getValues() {
                return values;
            }

            public void setValues() {
                try {
                    int lastPage = 4;
//                    Calendar mon = Calendar.getInstance();
//                    mon.add(Calendar.MONTH , -1);
//                    TimeZone seoul = TimeZone.getTimeZone("Asia/Seoul");
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
//                    sdf.setTimeZone(seoul);
//                    String today = sdf.format(mon.getTime());

                    for (int page = 1; page <= lastPage; page++) {
                        String url = "https://finance.naver.com/sise/sise_index_day.naver?code=KOSPI&page=" + page;
                        Document document = Jsoup.connect(url).get();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        for (int i = 3; i <= 5; i++) {
                            Elements dateElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td.date");
                            Elements numberElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td:nth-child(2)");
                            float dateTimeSecond = df.parse(dateElement.get(0).text()).getTime() / 1000;
                            float number = Float.valueOf(numberElement.get(0).text().replace(",", ""));
                            values.add(new Entry(dateTimeSecond, number));
                        }
                        for (int i = 10; i <= 12; i++) {
                            Elements dateElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td.date");
                            Elements numberElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td:nth-child(2)");
                            float dateTimeSecond = df.parse(dateElement.get(0).text()).getTime() / 1000;
                            float number = Float.valueOf(numberElement.get(0).text().replace(",", ""));
                            values.add(new Entry(dateTimeSecond, number));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void run() {
                this.setValues();
            }
        }

        //main
        KospiPrice kospiPrice = new KospiPrice();
        kospiPrice.start();
        try {
            kospiPrice.join();
            Collections.reverse(kospiPrice.getValues());
            return kospiPrice.getValues();
        } catch (InterruptedException e) {
            return kospiPrice.getValues();
        }
    }

    static public ArrayList<Entry> getKosdaqEntryMonth() {
        ArrayList<Entry> values = new ArrayList<>();

        class KosdaqPrice extends Thread {
            ArrayList<Entry> values = new ArrayList<>();
            public ArrayList<Entry> getValues() {
                return values;
            }

            public void setValues() {
                try {
                    int lastPage = 4;
//                    Calendar mon = Calendar.getInstance();
//                    mon.add(Calendar.MONTH , -1);
//                    TimeZone seoul = TimeZone.getTimeZone("Asia/Seoul");
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
//                    sdf.setTimeZone(seoul);
//                    String today = sdf.format(mon.getTime());

                    for (int page = 1; page <= lastPage; page++) {
                        String url = "https://finance.naver.com/sise/sise_index_day.naver?code=KOSDAQ&page=" + page;
                        Document document = Jsoup.connect(url).get();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        for (int i = 3; i <= 5; i++) {
                            Elements dateElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td.date");
                            Elements numberElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td:nth-child(2)");
                            float dateTimeSecond = df.parse(dateElement.get(0).text()).getTime() / 1000;
                            float number = Float.valueOf(numberElement.get(0).text().replace(",", ""));
                            values.add(new Entry(dateTimeSecond, number));
                        }
                        for (int i = 10; i <= 12; i++) {
                            Elements dateElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td.date");
                            Elements numberElement = document.select("body > div > table.type_1 > tbody > tr:nth-child(" + i + ") > td:nth-child(2)");
                            float dateTimeSecond = df.parse(dateElement.get(0).text()).getTime() / 1000;
                            float number = Float.valueOf(numberElement.get(0).text().replace(",", ""));
                            values.add(new Entry(dateTimeSecond, number));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void run() {
                this.setValues();
            }
        }

        //main
        KosdaqPrice kosdaqPrice = new KosdaqPrice();
        kosdaqPrice.start();
        try {
            kosdaqPrice.join();
            Collections.reverse(kosdaqPrice.getValues());
            return kosdaqPrice.getValues();
        } catch (InterruptedException e) {
            return kosdaqPrice.getValues();
        }
    }
}