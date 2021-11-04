package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainSettingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_setting, container, false);
        Button buttonLogout = (Button)rootView.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new onClickButtonLogout());
        return rootView;
    }

    class onClickButtonLogout implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity)getActivity();
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
    }
}