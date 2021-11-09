package com.stockchain.bcapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stockchain.cosmos.PreferenceManager;
import com.stockchain.cosmos.StockDataInform;
import com.stockchain.cosmos.StockTransactionInform;

import java.util.ArrayList;

public class MockStockTransactionStatusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mock_stock_transaction_status, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();

        //set asset
        TextView mockCurrentTotalAmount = rootView.findViewById(R.id.mockCurrentTotalAmount);
        mockCurrentTotalAmount.setText(String.valueOf(mainActivity.stockBankInform.getCurrentTotalAmount()));
        TextView mockTotalEarningRate = rootView.findViewById(R.id.mockTotalEarningRate);
        mockTotalEarningRate.setText(String.format("%.2f", mainActivity.stockBankInform.getEarningRate()));
        if(mainActivity.stockBankInform.getEarningRate()<0){
            mockTotalEarningRate.setText("-"+mockTotalEarningRate.getText());
            mockTotalEarningRate.setTextColor(Color.parseColor("#097df3"));
        }else{
            mockTotalEarningRate.setText("+"+mockTotalEarningRate.getText());
            mockTotalEarningRate.setTextColor(Color.parseColor("#ed3738"));
        }
        TextView mockBalances = rootView.findViewById(R.id.mockBalances);
        mockBalances.setText(String.valueOf(mainActivity.stockBankInform.getBalances()));
        TextView mockCurrentStockTotalAmount = rootView.findViewById(R.id.mockCurrentStockTotalAmount);
        mockCurrentStockTotalAmount.setText(String.valueOf(mainActivity.stockBankInform.getCurrentStockTotalAmount()));


        //set holding stock
        RecyclerView mockRecyclerView = rootView.findViewById(R.id.mockRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mockRecyclerView.setLayoutManager(layoutManager);

        MockStockTransactionStatusAdapter mockStockTransactionStatusAdapter = new MockStockTransactionStatusAdapter(mainActivity);
        mockStockTransactionStatusAdapter.setItems(mainActivity.stockTransactionInformList);
        mockRecyclerView.setAdapter(mockStockTransactionStatusAdapter);

        return rootView;
    }

    class MockStockTransactionStatusAdapter extends RecyclerView.Adapter<MockStockTransactionStatusAdapter.MockViewHolder>{
        ArrayList<StockTransactionInform> items = new ArrayList<>();
        MainActivity mainActivity;

        public MockStockTransactionStatusAdapter(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @NonNull
        @Override
        public MockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.holding_stock, viewGroup, false);
            return new MockViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MockViewHolder holder, int position) {
            StockTransactionInform item = items.get(position);
            holder.setItem(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(StockTransactionInform item){
            items.add(item);
        }

        public  void setItems(ArrayList<StockTransactionInform> items){
            this.items = items;
        }

        public void setItem(int position, StockTransactionInform item){
            items.set(position, item);
        }
        public StockTransactionInform getItem(int position){
            return items.get(position);
        }

        public class MockViewHolder extends RecyclerView.ViewHolder{
            TextView holdingStockName;
            TextView holdingStockEarningRate;
            TextView holdingStockCount;
            TextView holdingStockCurrentPrice;
            TextView holdingStockPurchasePrice;


            public MockViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition() ;
                        if (pos != RecyclerView.NO_POSITION) {
                            StockTransactionInform item= getItem(pos);
                            StockDataInform stockDataInform= new StockDataInform(item.getCode(), item.getName(), item.getMarketType(), (int) item.getCurrentAmount());
                            mainActivity.inqueryStockDataInform = stockDataInform;
                            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainActivity.mainStockInqueryFragment).addToBackStack(null).commit();

                        }
                    }
                });
                holdingStockName = itemView.findViewById(R.id.holdingStockName);
                holdingStockEarningRate = itemView.findViewById(R.id.holdingStockEarningRate);
                holdingStockCount = itemView.findViewById(R.id.holdingStockCount);
                holdingStockCurrentPrice = itemView.findViewById(R.id.holdingStockCurrentPrice);
                holdingStockPurchasePrice = itemView.findViewById(R.id.holdingStockPurchasePrice);
            }

            public void setItem(StockTransactionInform item){
                holdingStockName.setText(item.getName());
                holdingStockEarningRate.setText(String.format("%.2f", item.getEarningPrice()));
                if(item.getEarningPrice()<0){
                    holdingStockEarningRate.setText("-"+holdingStockEarningRate.getText());
                    holdingStockEarningRate.setTextColor(Color.parseColor("#097df3"));
                }else{
                    holdingStockEarningRate.setText("+"+holdingStockEarningRate.getText());
                    holdingStockEarningRate.setTextColor(Color.parseColor("#ed3738"));
                }
                holdingStockCount.setText(String.valueOf(item.getCount()+"주"));
                holdingStockCurrentPrice.setText(String.valueOf(item.getCurrentAmount()));
                if(item.getCurrentAmount()<item.getPurchaseAmount()){
                    holdingStockCurrentPrice.setTextColor(Color.parseColor("#097df3"));
                }else{
                    holdingStockCurrentPrice.setTextColor(Color.parseColor("#ed3738"));
                }
                holdingStockPurchasePrice.setText(String.valueOf(item.getPurchaseAmount()));

            }
        }
    }
}