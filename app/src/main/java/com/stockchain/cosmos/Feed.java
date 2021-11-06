package com.stockchain.cosmos;

import com.github.mikephil.charting.data.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Feed {
    public ArrayList<FeedInform> getRecentFeed(){
        ArrayList<FeedInform> feedInformArrayList = new ArrayList<>();

        class FeedThread extends Thread {
            public void run() {
                try {
                    String url = "https://finance.naver.com/news/news_list.naver?mode=LSS2D&section_id=101&section_id2=258&page=1";
                    Document document = Jsoup.connect(url).get();
                    Elements feedElement = document.select("#contentarea_left > ul > li.newsList.top > dl > .articleSubject,.articleSummary");
                    int i=0;
                    while(i<feedElement.size()){
                        if(!feedElement.get(i).className().equals("articleSubject")){
                            i++;
                            continue;
                        }
                        String subject = feedElement.get(i).text();
                        String newsURL = "https://finance.naver.com"+feedElement.get(i).select("a").attr("href").toString();
                        String summary = feedElement.get(i+1).text();
                        feedInformArrayList.add(new FeedInform(subject, summary, newsURL));
                        i+=2;
                    }

                    feedElement = document.select("#contentarea_left > ul > li:nth-child(2) > dl > .articleSubject,.articleSummary");
                    i=0;
                    while(i<feedElement.size()){
                        if(!feedElement.get(i).className().equals("articleSubject")){
                            i++;
                            continue;
                        }
                        String subject = feedElement.get(i).text();
                        String newsURL = "https://finance.naver.com"+feedElement.get(i).select("a").attr("href").toString();
                        String summary = feedElement.get(i+1).text();
                        feedInformArrayList.add(new FeedInform(subject, summary, newsURL));
                        i+=2;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FeedThread feedThread = new FeedThread();
        feedThread.start();
        try {
            feedThread.join();
            return feedInformArrayList;
        } catch (InterruptedException e) {
            return feedInformArrayList;
        }


    }
}
