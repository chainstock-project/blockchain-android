package com.stockchain.bcapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stockchain.cosmos.Account;
import com.stockchain.cosmos.BlockChain;
import com.stockchain.cosmos.PreferenceManager;
import com.stockchain.cosmos.WalletInform;

import java.io.IOException;
import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Thread thread = new LoadingThread();
        thread.start();
    }

    class LoadingThread extends Thread{
        @Override
        public void run() {
            TextView loading_meesage = findViewById(R.id.loading_message);
            BlockChain bc = new BlockChain(getApplicationContext(), getString(R.string.server_ip));

            PreferenceManager pm = new PreferenceManager();
//            bc.deleteConfig();
//            pm.removeKey(getApplicationContext(), "username");
//            pm.removeKey(getApplicationContext(), "address");

            loading_meesage.setText("check created config...");
            if(!bc.checkCreatedConfig()){
                loading_meesage.setText("createting config...");

                if(!bc.createConfig()){
                    bc.deleteConfig();
                    bc.createConfig();
                }
            }

            loading_meesage.setText("start node...");
            bc.startNode();

            while(true){
                int downloadedHeight = bc.getDownloadedHeight();
                double latestHeight = bc.getLatestHeight();

                String node_progress_message;
                if(latestHeight == Double.POSITIVE_INFINITY) {
                    node_progress_message = "Sync node...";
                }else{
                    node_progress_message = "height " + String.valueOf(downloadedHeight) + "/" + String.valueOf((int)latestHeight);
                }
                loading_meesage.setText(node_progress_message);

                if((latestHeight - downloadedHeight) < 2){
                    Account ac = new Account(getApplicationContext(), getString(R.string.server_ip));
                    String username = pm.getString(getApplicationContext(), "username");

                    if(ac.checkLogin(username)){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        }
    }
}