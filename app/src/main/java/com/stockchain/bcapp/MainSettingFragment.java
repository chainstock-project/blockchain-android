package com.stockchain.bcapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stockchain.cosmos.PreferenceManager;

public class MainSettingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_setting, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();

        TextView settingUsername = rootView.findViewById(R.id.settingUsername);
        settingUsername.setText(mainActivity.username);

        Button buttonLogout = (Button)rootView.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new onClickButtonLogout());
        return rootView;
    }

    class onClickButtonLogout implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity)getActivity();
            Context ctx = mainActivity.getApplicationContext();
            PreferenceManager pm = new PreferenceManager();
            pm.removeKey(ctx, "username");
            pm.removeKey(ctx, "address");
            mainActivity.ac.deleteAccount(mainActivity.username);

            Intent intent = new Intent(ctx, LoginActivity.class);
            startActivity(intent);
            mainActivity.finish();
        }
    }
}