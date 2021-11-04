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

import com.stockchain.cosmos.StockDataInform;
import com.stockchain.cosmos.StockTransactionInform;

import java.util.ArrayList;

public class MockStockTransactionStatusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mock_stock_transaction_status, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();

        mainActivity.mockRecyclerView = rootView.findViewById(R.id.mockRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mainActivity.mockRecyclerView.setLayoutManager(layoutManager);

        mainActivity.mockStatusAdapter = new MockStockTransactionStatusAdapter();
        mainActivity.mockStatusAdapter.setOnItemClickListener(new MockItemClickListener(mainActivity));

        ArrayList<StockTransactionInform> stockTransactionInformList = new ArrayList<>();
        stockTransactionInformList.add(new StockTransactionInform("00010",10,1000000,"hihi", "kospi",1000,10.5));
        stockTransactionInformList.add(new StockTransactionInform("00010",10,1000000,"hihi", "kospi",1000,10.5));
        stockTransactionInformList.add(new StockTransactionInform("00010",10,1000000,"hihi", "kospi",1000,10.5));
        stockTransactionInformList.add(new StockTransactionInform("00010",10,1000000,"hihi", "kospi",1000,10.5));

        mainActivity.mockStatusAdapter.setItems(stockTransactionInformList);
        mainActivity.mockRecyclerView.setAdapter(mainActivity.mockStatusAdapter);

        return rootView;
    }
}


class MockStockTransactionStatusAdapter extends RecyclerView.Adapter<MockStockTransactionStatusAdapter.MockViewHolder>{
    ArrayList<StockTransactionInform> items = new ArrayList<>();
    private OnItemClickListener mListener = null ;

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
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
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
                        mListener.onItemClick(view, pos);

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
            holdingStockEarningRate.setText(String.valueOf(item.getEarningPrice()));
            holdingStockCount.setText(String.valueOf(item.getCount()+"주"));
            holdingStockCurrentPrice.setText(String.valueOf(item.getCurrentAmount()));
            holdingStockPurchasePrice.setText(String.valueOf(item.getPurchaseAmount()));

        }
    }
}

class MockItemClickListener implements OnItemClickListener {
    MainActivity mainActivity;

    public MockItemClickListener(MainActivity mainActivity) {
        this.mainActivity= mainActivity;
    }

    @Override
    public void onItemClick(View v, int position) {
        StockTransactionInform item= mainActivity.mockStatusAdapter.getItem(position);
        StockDataInform stockDataInform= new StockDataInform(item.getCode(), item.getName(), item.getMarketType());
        mainActivity.inqueryStockDataInform = stockDataInform;
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainActivity.mainStockInqueryFragment).addToBackStack(null).commit();
    }
}