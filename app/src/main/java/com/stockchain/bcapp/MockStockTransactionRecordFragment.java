package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stockchain.cosmos.StockTransaction;
import com.stockchain.cosmos.StockTransactionInform;
import com.stockchain.cosmos.StockTransactionRecordInform;

import java.io.IOException;
import java.util.ArrayList;

/**
 */
public class MockStockTransactionRecordFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mock_stock_transaction_record, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();

        RecyclerView mockRecordRecyclerView = rootView.findViewById(R.id.mockRecordRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mockRecordRecyclerView.setLayoutManager(layoutManager);

        MockStockTransactionRecordAdapter mockRecordAdapter = new MockStockTransactionRecordAdapter();
        mockRecordAdapter.setItems(mainActivity.stockTransactionRecordInformList);
        mockRecordRecyclerView.setAdapter(mockRecordAdapter);

        return rootView;
    }

    class MockStockTransactionRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        ArrayList<StockTransactionRecordInform> items = new ArrayList<>();

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = null;
            if(viewType == 0){
                itemView = inflater.inflate(R.layout.mock_stock_transaction_record_buy, viewGroup, false);
                return new MockRecordViewHolderBuy(itemView);
            }
            else if(viewType == 1){
                itemView = inflater.inflate(R.layout.mock_stock_transaction_record_sell, viewGroup, false);
                return new MockRecordViewHolderSell(itemView);
            }
            else{
                itemView = inflater.inflate(R.layout.mock_stock_transaction_record_date, viewGroup, false);
                return new MockRecordViewHolderDate(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            StockTransactionRecordInform item = items.get(position);
            if(holder instanceof MockRecordViewHolderBuy)
            {
                ((MockRecordViewHolderBuy) holder).setItem(item);
            }
            else if(holder instanceof MockRecordViewHolderSell)
            {
                ((MockRecordViewHolderSell) holder).setItem(item);
            }
            else
            {
                ((MockRecordViewHolderDate) holder).setItem(item);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            StockTransactionRecordInform item = items.get(position);
            if(item.getRecordType().equals("BUY")){
                return 0;
            }else if(item.getRecordType().equals("SELL")){
                return 1;
            }
            else{
                return 2;
            }
        }

        public void addItem(StockTransactionRecordInform item){
            items.add(item);
        }

        public  void setItems(ArrayList<StockTransactionRecordInform> items){
            this.items = items;
        }

        public void setItem(int position, StockTransactionRecordInform item){
            items.set(position, item);
        }

        public StockTransactionRecordInform getItem(int position){
            return items.get(position);
        }

        public class MockRecordViewHolderBuy extends RecyclerView.ViewHolder{
            TextView recordBuyName;
            TextView recordBuyPrice;
            TextView recordBuyCount;

            public MockRecordViewHolderBuy(@NonNull View itemView) {
                super(itemView);
                recordBuyName = itemView.findViewById(R.id.recordBuyName);
                recordBuyPrice = itemView.findViewById(R.id.recordBuyPrice);
                recordBuyCount = itemView.findViewById(R.id.recordBuyCount);
            }

            public void setItem(StockTransactionRecordInform item){
                recordBuyName.setText(item.getName());
                recordBuyPrice.setText("매수가 "+String.valueOf(item.getAmount()));
                recordBuyCount.setText(String.valueOf(item.getCount())+"주");
            }
        }

        public class MockRecordViewHolderSell extends RecyclerView.ViewHolder{
            TextView recordSellName;
            TextView recordSellPrice;
            TextView recordSellCount;

            public MockRecordViewHolderSell(@NonNull View itemView) {
                super(itemView);
                recordSellName = itemView.findViewById(R.id.recordSellName);
                recordSellPrice = itemView.findViewById(R.id.recordSellPrice);
                recordSellCount = itemView.findViewById(R.id.recordSellCount);
            }

            public void setItem(StockTransactionRecordInform item){
                recordSellName.setText(item.getName());
                recordSellPrice.setText("매도가 " +String.valueOf(item.getAmount()));
                recordSellCount.setText(String.valueOf(item.getCount())+"주");

            }
        }
        public class MockRecordViewHolderDate extends RecyclerView.ViewHolder{
            TextView recordDate;

            public MockRecordViewHolderDate(@NonNull View itemView) {
                super(itemView);
                recordDate = itemView.findViewById(R.id.recordDate);
            }

            public void setItem(StockTransactionRecordInform item){
                recordDate.setText(item.getDate());
            }
        }
    }
}