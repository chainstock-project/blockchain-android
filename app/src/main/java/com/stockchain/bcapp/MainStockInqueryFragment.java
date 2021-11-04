package com.stockchain.bcapp;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.zip.ZipEntry;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainStockInqueryFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_stock_inquery, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        SearchView searchView = mainActivity.getSearchView();
        if(searchView != null) {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            ActionBar actionBar = mainActivity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }

        Button buttonRed = (Button) rootView.findViewById(R.id.buttonStockTransaction) ;
        buttonRed.setOnClickListener(new onClickButtonStockTransaction()) ;
        mainActivity.inqueryStockDataInform.getCode();

        return rootView;
    }

    class onClickButtonStockTransaction implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity)getActivity();
            MainStockTransactionFragment mainStockTransactionFragment = new MainStockTransactionFragment();
            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainStockTransactionFragment).addToBackStack(null).commit();
        }
    }
}