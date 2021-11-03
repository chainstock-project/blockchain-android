package com.stockchain.bcapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stockchain.cosmos.PreferenceManager;
import com.stockchain.cosmos.StockBankInform;
import com.stockchain.cosmos.StockTransaction;
import com.stockchain.cosmos.StockTransactionInform;

import java.io.IOException;
import java.util.ArrayList;

public class MainHomeFragment extends Fragment {
    private LineChart kospiChart;
    private LineChart kosdaqChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_main_home, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        SearchView searchView = mainActivity.getSearchView();
        if(searchView != null){
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            ActionBar actionBar = mainActivity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

        //set asset
        try {
            Context ctx = getActivity().getApplicationContext();
            PreferenceManager pm = new PreferenceManager();
            String username = pm.getString(ctx, "username");
            String address = pm.getString(ctx, "address");

            StockTransaction stockTransaction = new StockTransaction(ctx);

            ArrayList<StockTransactionInform> stockTransactionInformList = stockTransaction.getStockTransactionInformList(address);
            StockBankInform stockBankInform = stockTransaction.getStockBankInform(stockTransactionInformList, address);
            ArrayList<StockTransactionInform> stockTransactionTop3 = stockTransaction.getStockTransactionTop3(stockTransactionInformList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set graph
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        LineData data = new LineData(dataSets);
        kospiChart= (LineChart)rootview.findViewById(R.id.kospiChart);
        kospiChart.setData(data);

        values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }
        set1 = new LineDataSet(values, "DataSet 1");
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        data = new LineData(dataSets);
        kosdaqChart= (LineChart)rootview.findViewById(R.id.kosdaqChart);
        kosdaqChart.setData(data);

        return rootview;
    }
}