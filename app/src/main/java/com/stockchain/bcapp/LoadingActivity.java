package com.stockchain.bcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stockchain.cosmos.Account;
import com.stockchain.cosmos.BlockChain;
import com.stockchain.cosmos.PreferenceManager;

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
                    finishAffinity();
                    System.exit(0);
                }
            }

            loading_meesage.setText("start node...");
            bc.startNode();

            while(true){
                int downloadedHeight = bc.getDownloadedHeight();
                double latestHeight = bc.getLatestHeight();
                if(latestHeight == Double.POSITIVE_INFINITY) {
                    loading_meesage.setText("Sync node...");
                }else{
                    loading_meesage.setText("height " + String.valueOf(downloadedHeight) + "/" + String.valueOf((int)latestHeight));
                }

                if((latestHeight - downloadedHeight) < 2){
                    Account ac = new Account(getApplicationContext(), getString(R.string.server_ip));
                    String username = pm.getString(getApplicationContext(), "username");
                    if((username==null) || (ac.checkCreatedAccountEqualRegisteredAccountByUsername(username)==false)){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        }
    }
}