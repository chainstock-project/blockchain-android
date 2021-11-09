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
import com.stockchain.cosmos.LineChartXAxisValueDateFormatter;
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
        MainActivity mainActivity = (MainActivity)getActivity();

        //set asset
        TextView assetCurrentTotalAmount = rootView.findViewById(R.id.assetCurrentTotalAmount);
        assetCurrentTotalAmount.setText(String.valueOf(mainActivity.stockBankInform.getCurrentTotalAmount()));
        TextView assetTotalEarningPrice = rootView.findViewById(R.id.assetTotalEarningPrice);
        assetTotalEarningPrice.setText(String.format("%.2f", mainActivity.stockBankInform.getEarningRate()));
        if(mainActivity.stockBankInform.getEarningRate()<0){
            assetTotalEarningPrice.setText("-"+assetTotalEarningPrice.getText());
            assetTotalEarningPrice.setTextColor(Color.parseColor("#097df3"));
        }else{
            assetTotalEarningPrice.setText("+"+assetTotalEarningPrice.getText());
            assetTotalEarningPrice.setTextColor(Color.parseColor("#ed3738"));
        }
        TextView assetBalances = rootView.findViewById(R.id.assetBalances);
        assetBalances.setText(String.valueOf(mainActivity.stockBankInform.getBalances()));
        TextView assetCurrentStockTotalAmount = rootView.findViewById(R.id.assetCurrentStockTotalAmount);
        assetCurrentStockTotalAmount.setText(String.valueOf(mainActivity.stockBankInform.getCurrentStockTotalAmount()));

        //set kospi graph
        kospiChart= (LineChart)rootView.findViewById(R.id.kospiChart);
        ArrayList<Entry> kospiEntryMonth = StockData.getKospiEntryMonth();
        LineDataSet kospiSet1 = new LineDataSet(kospiEntryMonth, "price");
        kospiSet1.setColor(Color.RED);
        kospiSet1.setDrawCircles(false);
        kospiSet1.setDrawValues(false);

        ArrayList<ILineDataSet> kospiDataSets = new ArrayList<>();
        kospiDataSets.add(kospiSet1); // add the data sets
        LineData kospiData = new LineData(kospiDataSets);
        XAxis kospiXAxis= kospiChart.getXAxis();
        kospiXAxis.setValueFormatter(new LineChartXAxisValueDateFormatter());
        kospiXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        kospiChart.setData(kospiData);

        //set kosdaq graph
        kosdaqChart= (LineChart)rootView.findViewById(R.id.kosdaqChart);
        ArrayList<Entry> kosdaqEntryMonth = StockData.getKosdaqEntryMonth();
        LineDataSet kosdaqSet1 = new LineDataSet(kosdaqEntryMonth, "price");
        kosdaqSet1.setColor(Color.RED);
        kosdaqSet1.setDrawCircles(false);
        kosdaqSet1.setDrawValues(false);
        ArrayList<ILineDataSet> kosdaqDataSets = new ArrayList<>();
        kosdaqDataSets.add(kosdaqSet1); // add the data sets
        LineData kosdaqData = new LineData(kosdaqDataSets);
        XAxis kosdaqXAxis= kosdaqChart.getXAxis();
        kosdaqXAxis.setValueFormatter(new LineChartXAxisValueDateFormatter());
        kosdaqXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        kosdaqChart.setData(kosdaqData);


        return rootView;
    }
}