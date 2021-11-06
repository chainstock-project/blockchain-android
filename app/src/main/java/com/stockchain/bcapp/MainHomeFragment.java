package com.stockchain.bcapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.stockchain.cosmos.PreferenceManager;
import com.stockchain.cosmos.StockBankInform;
import com.stockchain.cosmos.StockData;
import com.stockchain.cosmos.StockTransaction;
import com.stockchain.cosmos.StockTransactionInform;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainHomeFragment extends Fragment {
    private LineChart kospiChart;
    private LineChart kosdaqChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_home, container, false);


        //set asset
        Context ctx = getActivity().getApplicationContext();
        PreferenceManager pm = new PreferenceManager();
        String username = pm.getString(ctx, "username");
        String address = pm.getString(ctx, "address");
        try {
            StockTransaction stockTransaction = new StockTransaction(ctx);
            ArrayList<StockTransactionInform> stockTransactionInformList = stockTransaction.getStockTransactionInformList(address);

            StockBankInform stockBankInform = stockTransaction.getStockBankInform(stockTransactionInformList, address);

            TextView assetCurrentTotalAmount = rootView.findViewById(R.id.assetCurrentTotalAmount);
            assetCurrentTotalAmount.setText(String.valueOf(stockBankInform.getCurrentTotalAmount()));

            TextView assetTotalEarningPrice = rootView.findViewById(R.id.assetTotalEarningPrice);
            assetTotalEarningPrice.setText(String.format("%.2f", stockBankInform.getEarningRate()));
            if(stockBankInform.getEarningRate()<0){
                assetTotalEarningPrice.setTextColor(Color.parseColor("#ed3738"));
            }else{
                assetTotalEarningPrice.setTextColor(Color.parseColor("#097df3"));
            }

            TextView assetBalances = rootView.findViewById(R.id.assetBalances);
            assetBalances.setText(String.valueOf(stockBankInform.getBalances()));

            TextView assetCurrentStockTotalAmount = rootView.findViewById(R.id.assetCurrentStockTotalAmount);
            assetCurrentStockTotalAmount.setText(String.valueOf(stockBankInform.getCurrentStockTotalAmount()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set graph
        kospiChart= (LineChart)rootView.findViewById(R.id.kospiChart);

        StockData stockData = new StockData(rootView.getContext());
        ArrayList<Entry> kospiEntryMonth = stockData.getKospiEntryMonth();
        LineDataSet kospiSet1 = new LineDataSet(kospiEntryMonth, "kospi price 1");
        kospiSet1.setColor(Color.RED);
        kospiSet1.setDrawCircles(false);
        ArrayList<ILineDataSet> kospiDataSets = new ArrayList<>();
        kospiDataSets.add(kospiSet1); // add the data sets

        LineData kospiData = new LineData(kospiDataSets);
        XAxis kospiXAxis= kospiChart.getXAxis();
        kospiXAxis.setValueFormatter(new LineChartXAxisValueFormatter());
        kospiXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        kospiChart.setData(kospiData);

        kosdaqChart= (LineChart)rootView.findViewById(R.id.kosdaqChart);
        ArrayList<Entry> kosdaqEntryMonth = stockData.getKosdaqEntryMonth();
        LineDataSet kosdaqSet1 = new LineDataSet(kosdaqEntryMonth, "DataSet 1");
        kosdaqSet1.setColor(Color.RED);
        kosdaqSet1.setDrawCircles(false);
        ArrayList<ILineDataSet> kosdaqDataSets = new ArrayList<>();
        kosdaqDataSets.add(kosdaqSet1); // add the data sets

        LineData kosdaqData = new LineData(kosdaqDataSets);
        XAxis kosdaqXAxis= kosdaqChart.getXAxis();
        kosdaqXAxis.setValueFormatter(new LineChartXAxisValueFormatter());
        kosdaqXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        kosdaqChart.setData(kosdaqData);

        return rootView;
    }
}


class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        Date date = new Date();
        date.setTime((long) value*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        return sdf.format(date);
    }
}