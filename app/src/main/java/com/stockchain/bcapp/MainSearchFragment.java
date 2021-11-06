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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_search, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
        bottomNavigationView.setVisibility(View.INVISIBLE);


        mainActivity.searchRecyclerView = rootView.findViewById(R.id.searchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mainActivity.searchRecyclerView.setLayoutManager(layoutManager);
        mainActivity.searchAdapter = new SearchAdapter();
        mainActivity.searchAdapter.setOnItemClickListener(new SearchVeiwItemClickListener(mainActivity));
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
}

class onQueryTextSearchView implements SearchView.OnQueryTextListener{
    MainActivity mainActivity;
    ArrayList<StockDataInform> stockDataList;

    onQueryTextSearchView(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.stockDataList = mainActivity.stockDataList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(mainActivity.searchAdapter.getItemCount()>0){
            mainActivity.inqueryStockDataInform = mainActivity.searchAdapter.getItem(0);
            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainActivity.mainStockInqueryFragment).addToBackStack(null).commit();
        }
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            ArrayList<StockDataInform> stockDataInforms = StockData.searchStock(stockDataList, newText);
            mainActivity.searchAdapter.setItems(stockDataInforms);
            mainActivity.searchRecyclerView.setAdapter(mainActivity.searchAdapter);
            return true;
        }catch (NullPointerException e){
            return true;
        }
    }
}

class SearchVeiwItemClickListener implements OnItemClickListener {
    MainActivity mainActivity;

    public SearchVeiwItemClickListener(MainActivity mainActivity) {
        this.mainActivity= mainActivity;
    }

    @Override
    public void onItemClick(View v, int position) {
        mainActivity.inqueryStockDataInform = mainActivity.searchAdapter.getItem(position);
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainActivity.mainStockInqueryFragment).addToBackStack(null).commit();
    }
}


class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    ArrayList<StockDataInform> items = new ArrayList<>();
    private OnItemClickListener mListener = null ;

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
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
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
                        mListener.onItemClick(view, pos);

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
