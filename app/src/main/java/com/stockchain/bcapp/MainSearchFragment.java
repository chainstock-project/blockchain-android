package com.stockchain.bcapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stockchain.cosmos.StockDataInform;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainSearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_search, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
        bottomNavigationView.setVisibility(View.INVISIBLE);

        mainActivity.recyclerView = rootView.findViewById(R.id.searchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mainActivity.recyclerView.setLayoutManager(layoutManager);
        mainActivity.searchAdapter = new SearchAdapter();
        mainActivity.searchAdapter.setOnItemClickListener(new SearchVeiwItemClick());

        return rootView;
    }
}

class onQueryTextSearchView implements SearchView.OnQueryTextListener{
    MainActivity mainActivity;

    onQueryTextSearchView(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            SearchAdapter searchAdapter = mainActivity.searchAdapter;
            searchAdapter.addItem(new StockDataInform("0001", newText, "kospi"));
            mainActivity.recyclerView.setAdapter(searchAdapter);
            return true;
        }catch (NullPointerException e){
            return true;
        }
    }
}

class SearchVeiwItemClick implements SearchAdapter.OnItemClickListener {
    @Override
    public void onItemClick(View v, int position) {
        // TODO : 아이템 클릭 이벤트를 MainActivity에서 처리.
        TextView stockDataCodeView = v.findViewById(R.id.stockDataCode);
        stockDataCodeView.setText("complete");
    }
}