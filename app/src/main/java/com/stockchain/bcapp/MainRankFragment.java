package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.stockchain.cosmos.RankInform;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainRankFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_rank, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();

        mainActivity.rankRecyclerView = rootView.findViewById(R.id.rankRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mainActivity.rankRecyclerView.setLayoutManager(layoutManager);

        mainActivity.rankAdapter = new RankAdapter();
        ArrayList<RankInform> rankInformList = new ArrayList<>();
        rankInformList.add(new RankInform(1, "kim", 100000));
        rankInformList.add(new RankInform(2, "kim", 100000));
        rankInformList.add(new RankInform(3, "kim", 100000));
        mainActivity.rankAdapter.setItems(rankInformList);
        mainActivity.rankRecyclerView.setAdapter(mainActivity.rankAdapter);

        return rootView;
    }
}

class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder>{
    ArrayList<RankInform> items = new ArrayList<>();

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.rank_inform, viewGroup, false);
        return new RankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        RankInform item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(RankInform item){
        items.add(item);
    }

    public  void setItems(ArrayList<RankInform> items){
        this.items = items;
    }

    public void setItem(int position, RankInform item){
        items.set(position, item);
    }
    public RankInform getItem(int position){
        return items.get(position);
    }

    public class RankViewHolder extends RecyclerView.ViewHolder{
        TextView rankRanking;
        TextView rankUserName;
        TextView rankAmount;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            rankRanking = itemView.findViewById(R.id.rankRanking);
            rankUserName = itemView.findViewById(R.id.rankUsername);
            rankAmount = itemView.findViewById(R.id.rankAmount);
        }

        public void setItem(RankInform item){
            rankRanking.setText(String.valueOf(item.getRanking()));
            rankUserName.setText(item.getUsername());
            rankAmount.setText(String.valueOf(item.getAmount()));
        }
    }
}
