package com.stockchain.bcapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainMockFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_mock, container, false);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabMock);
        ViewPager pager= rootView.findViewById(R.id.mockContainer);

        MockPageAdapter mockPageAdapter = new MockPageAdapter(getChildFragmentManager());
        mockPageAdapter.addItem(new MockTransactionStatusFragment());
        mockPageAdapter.addItem(new MockTransactionRecordFragment());
        pager.setAdapter(mockPageAdapter);

        tabLayout.setupWithViewPager(pager); //텝레이아웃과 뷰페이저를 연결
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    class MockPageAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<>();

        public MockPageAdapter(FragmentManager fm) {
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

        public CharSequence getPageTitle(int position) {
            if (position == 0) { //텝 레이아웃의 타이틀 설정
                return "보유현황";
            } else {
                return "거래내역";
            }
        }
    }
}