package com.stockchain.cosmos;

import android.content.Context;

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
    private final String blockchainPath;
    private final String homeDir;

    public StockData(Context ctx) {
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
        line_split = line.split(" ");
        String name = line_split[line_split.length - 1];

        StockDataInform stockDataInform = new StockDataInform(get_code, name, market_type, amount, date);
        return stockDataInform;
    }

    public StockDataDetailInform getStockDetail(String code) {
        StockDataDetailInform stockDataDetailInform = new StockDataDetailInform();
        return stockDataDetailInform;
    }

    public ArrayList<StockDataInform> searchStock(ArrayList<StockDataInform> stockDataInformList, String name) {
        ArrayList<StockDataInform> searchedStockDataInformList = new ArrayList<>();

        Iterator<StockDataInform> iterator = stockDataInformList.iterator();
        while (iterator.hasNext()) {
            StockDataInform stockDataInform = iterator.next();
            if (stockDataInform.name.contains(name)) {
                searchedStockDataInformList.add(stockDataInform);
            }
        }

        return searchedStockDataInformList;
    }

    public ArrayList<Entry> getKospiEntryMonth() {
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

    public ArrayList<Entry> getKosdaqEntryMonth() {
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