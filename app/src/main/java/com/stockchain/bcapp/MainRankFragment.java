package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainRankFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_main_rank, container, false);
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

        return rootview;    }
}