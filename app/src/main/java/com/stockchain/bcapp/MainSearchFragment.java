package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        RecyclerView recyclerView = rootView.findViewById(R.id.searchRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SearchAdapter searchAdapter = new SearchAdapter();
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // TODO : 아이템 클릭 이벤트를 MainActivity에서 처리.
                TextView stockDataCodeView = v.findViewById(R.id.stockDataCode);
                stockDataCodeView.setText("complete");
            }
        }) ;
        searchAdapter.addItem(new StockDataInform("0001", "삼성전자", "kospi"));
        searchAdapter.addItem(new StockDataInform("0002", "삼성전자", "kospi"));
        searchAdapter.addItem(new StockDataInform("0003", "삼성전자", "kospi"));
        recyclerView.setAdapter(searchAdapter);

        return rootView;
    }
}