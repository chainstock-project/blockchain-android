package com.stockchain.bcapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stockchain.cosmos.StockDataInform;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
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

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
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
