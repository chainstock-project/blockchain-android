package com.stockchain.bcapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.stockchain.cosmos.Feed;
import com.stockchain.cosmos.FeedInform;

import java.util.ArrayList;

public class MainFeedFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_feed, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();

        mainActivity.feedRecyclerView = rootView.findViewById(R.id.feedRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mainActivity.feedRecyclerView.setLayoutManager(layoutManager);
        mainActivity.feedAdapter = new FeedAdapter();
        mainActivity.feedAdapter.setOnItemClickListener(new FeedItemClickListener(mainActivity));

        Feed feed = new Feed();
        ArrayList<FeedInform> FeedInformList = feed.getRecentFeed();
        mainActivity.feedAdapter.setItems(FeedInformList);
        mainActivity.feedRecyclerView.setAdapter(mainActivity.feedAdapter);

        return rootView;
    }
}

class FeedItemClickListener implements OnItemClickListener {
    MainActivity mainActivity;

    public FeedItemClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onItemClick(View v, int position) {
        FeedInform feedInform = mainActivity.feedAdapter.getItem(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedInform.getUrl()));
        v.getContext().startActivity(intent);
    }
}

class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    ArrayList<FeedInform> items = new ArrayList<>();
    private OnItemClickListener mListener = null ;

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.feed_inform, viewGroup, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedInform item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FeedInform item){
        items.add(item);
    }

    public  void setItems(ArrayList<FeedInform> items){
        this.items = items;
    }

    public void setItem(int position, FeedInform item){
        items.set(position, item);
    }
    public FeedInform getItem(int position){
        return items.get(position);
    }
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(FeedItemClickListener listener) {
        this.mListener = listener ;
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder{
        TextView feedSubject;
        TextView feedSummary;

        public FeedViewHolder(@NonNull View itemView) {
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

            feedSubject = itemView.findViewById(R.id.feedTitle);
            feedSummary = itemView.findViewById(R.id.feedContent);
        }

        public void setItem(FeedInform item){
            feedSubject.setText(item.getSubject());
            feedSummary.setText(item.getSummary());
        }
    }
}
