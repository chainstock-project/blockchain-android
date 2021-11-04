package com.stockchain.bcapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainStockTransactionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_stock_transaction, container, false);

        // Inflate the layout for this fragment
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabStockTransaction);
        ViewPager pager = rootView.findViewById(R.id.stockTransactionContainer);
        PageAdapter pageAdapter = new PageAdapter(getParentFragmentManager());
        pageAdapter.addItem(new StockTransactionBuyFragment());
        pageAdapter.addItem(new StockTransactionSellFragment());
        pager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(pager); //텝레이아웃과 뷰페이저를 연결

        return rootView;
    }

    class PageAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<>();

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }
        public void addItem(Fragment item){
            items.add(item);
        }
        public Fragment getItem(int position){
            return items.get(position);
        }

        public int getCount(){
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) { //텝 레이아웃의 타이틀 설정
                return "매수";
            } else {
                return "매도";
            }
        }
    }
}