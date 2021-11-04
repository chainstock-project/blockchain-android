package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class StockTransactionBuyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_stock_transaction, container, false);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_transaction_buy, container, false);
    }
}