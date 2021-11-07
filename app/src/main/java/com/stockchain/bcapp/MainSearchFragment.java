package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stockchain.cosmos.StockData;
import com.stockchain.cosmos.StockDataInform;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class    MainSearchFragment extends Fragment {
    RecyclerView searchRecyclerView;
    SearchAdapter searchAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_search, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
        bottomNavigationView.setVisibility(View.INVISIBLE);

        mainActivity.searchView.setOnQueryTextListener(new onQueryTextSearchView(mainActivity));

        searchRecyclerView = rootView.findViewById(R.id.searchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter(mainActivity);
        searchRecyclerView.setAdapter(searchAdapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity mainActivity = (MainActivity)getActivity();
        SearchView searchView = mainActivity.getSearchView();
        if(searchView != null) {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            ActionBar actionBar = mainActivity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            MenuItem item = mainActivity.menu.findItem(R.id.menu_search);
            item.setVisible(true);
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    class onQueryTextSearchView implements SearchView.OnQueryTextListener{
        MainActivity mainActivity;

        onQueryTextSearchView(MainActivity mainActivity){
            this.mainActivity = mainActivity;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            if(searchAdapter.getItemCount()>0){
                mainActivity.inqueryStockDataInform = searchAdapter.getItem(0);
                mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainActivity.mainStockInqueryFragment).addToBackStack(null).commit();
            }
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            try {
                ArrayList<StockDataInform> stockDataInforms = StockData.searchStock(mainActivity.stockDataList, newText);
                searchAdapter.setItems(stockDataInforms);
                searchRecyclerView.setAdapter(searchAdapter);
                return true;
            }catch (NullPointerException e){
                return true;
            }
        }
    }

    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
        ArrayList<StockDataInform> items = new ArrayList<>();
        MainActivity mainActivity;

        public SearchAdapter(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.stock_data_inform, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            StockDataInform item = items.get(position);
            holder.setItem(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(StockDataInform item){
            items.add(item);
        }

        public  void setItems(ArrayList<StockDataInform> items){
            this.items = items;
        }

        public void setItem(int position, StockDataInform item){
            items.set(position, item);
        }
        public StockDataInform getItem(int position){
            return items.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView stockDataCodeView;
            TextView stockDataNameView;
            TextView stockDataMarketTypeView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition() ;
                        if (pos != RecyclerView.NO_POSITION) {
                            mainActivity.inqueryStockDataInform = getItem(pos);
                            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainActivity.mainStockInqueryFragment).addToBackStack(null).commit();

                        }
                    }
                });
                stockDataCodeView = itemView.findViewById(R.id.stockDataCode);
                stockDataNameView = itemView.findViewById(R.id.stockDataName);
                stockDataMarketTypeView = itemView.findViewById(R.id.stockDataMarketType);
            }

            public void setItem(StockDataInform item){
                stockDataCodeView.setText(item.getCode());
                stockDataNameView.setText(item.getName());
                stockDataMarketTypeView.setText(item.getMarket_type());
            }
        }
    }
}