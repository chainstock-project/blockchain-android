package com.stockchain.bcapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stockchain.cosmos.LineChartXAxisValueDateFormatter;
import com.stockchain.cosmos.StockData;
import com.stockchain.cosmos.StockDataDetailInform;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainStockInqueryFragment extends Fragment{
    private LineChart stockChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_stock_inquery, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        MenuItem item = mainActivity.menu.findItem(R.id.menu_search);
        item.setVisible(false);
        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
        bottomNavigationView.setVisibility(View.INVISIBLE);


        Button buttonStockTransaction = (Button) rootView.findViewById(R.id.buttonStockTransaction) ;
        buttonStockTransaction.setOnClickListener(new onClickButtonStockTransaction()) ;

        StockData stockData = new StockData(mainActivity.getApplicationContext());
        StockDataDetailInform stockDataDetailInform = StockData.getStockDataDetail(mainActivity.inqueryStockDataInform, mainActivity.stockTransactionInformList);
        TextView textStockName = rootView.findViewById(R.id.textStockName);
        textStockName.setText(stockDataDetailInform.getName());
        TextView textMarketType = rootView.findViewById(R.id.textMarketType);
        textMarketType.setText(stockDataDetailInform.getMarket_type());

        TextView textDayOverDayAmount = rootView.findViewById(R.id.textDayOverDayAmount);
        textDayOverDayAmount.setText(stockDataDetailInform.getDayOverDayAmount());
        TextView textDayOverDayRate = rootView.findViewById(R.id.textDayOverDayRate);
        textDayOverDayRate.setText(stockDataDetailInform.getDayOverDayRate());

        if(Float.parseFloat(stockDataDetailInform.getDayOverDayRate()) < 0){
            textDayOverDayAmount.setTextColor(Color.parseColor("#ed3738"));
            textDayOverDayRate.setTextColor(Color.parseColor("#ed3738"));
        }
        else{
            textDayOverDayAmount.setTextColor(Color.parseColor("#097df3"));
            textDayOverDayRate.setTextColor(Color.parseColor("#097df3"));
        }

        TextView textPreviousCloseAmount = rootView.findViewById(R.id.previousCloseAmount);
        textPreviousCloseAmount.setText(stockDataDetailInform.getPreviousCloseAmount());
        TextView numberOfStock = rootView.findViewById(R.id.numberOfStock);
        numberOfStock.setText(stockDataDetailInform.getNumberOfStock());
        TextView marketSum = rootView.findViewById(R.id.marketSum);
        marketSum.setText(stockDataDetailInform.getMarketSum());
        TextView marketRank = rootView.findViewById(R.id.marketRank);
        marketRank.setText(stockDataDetailInform.getMarketSumRanking());
        TextView faceValue = rootView.findViewById(R.id.faceValue);
        faceValue.setText(stockDataDetailInform.getFaceValue());
        TextView tradingUnit = rootView.findViewById(R.id.tradingUnit);
        tradingUnit.setText(stockDataDetailInform.getTradingUnit());
        TextView PER = rootView.findViewById(R.id.PER);
        PER.setText(stockDataDetailInform.getPER());
        TextView EPS = rootView.findViewById(R.id.EPS);
        EPS.setText(stockDataDetailInform.getEPS());
        TextView PBR = rootView.findViewById(R.id.PBR);
        PBR.setText(stockDataDetailInform.getPBR());
        TextView BPS = rootView.findViewById(R.id.BPS);
        BPS.setText(stockDataDetailInform.getBPS());
        TextView inderstryPER = rootView.findViewById(R.id.inderstryPER);
        inderstryPER.setText(stockDataDetailInform.getSameInderstryPER());


        stockChart = (LineChart)rootView.findViewById(R.id.stockChart);
        ArrayList<Entry> stockPrice = stockData.getStockPrice(mainActivity.inqueryStockDataInform.getCode());
        LineDataSet stockSet1 = new LineDataSet(stockPrice, "price");
        stockSet1.setColor(Color.RED);
        stockSet1.setDrawCircles(false);
        stockSet1.setDrawValues(false);
        ArrayList<ILineDataSet> stockDataSet = new ArrayList<>();
        stockDataSet.add(stockSet1); // add the data sets
        LineData stockDataLine = new LineData(stockDataSet);
        XAxis stockXAxis= stockChart.getXAxis();
        stockXAxis.setValueFormatter(new LineChartXAxisValueDateFormatter());
        stockXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        stockChart.setData(stockDataLine);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity mainActivity = (MainActivity)getActivity();
        SearchView searchView = mainActivity.getSearchView();
        if(searchView != null) {
            ActionBar actionBar = mainActivity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            MenuItem item = mainActivity.menu.findItem(R.id.menu_search);
            item.setVisible(true);
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    class onClickButtonStockTransaction implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.mainStockTransactionFragment = new MainStockTransactionFragment();
            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainActivity.mainStockTransactionFragment).addToBackStack(null).commit();
        }
    }
}