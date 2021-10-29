package com.example.bcapp;

import android.content.Context;
import android.os.Bundle;

import com.example.stockchain.Account;
import com.example.stockchain.BlockChain;
import com.example.stockchain.StockBankInform;
import com.example.stockchain.StockData;
import com.example.stockchain.StockDataInform;
import com.example.stockchain.StockTransaction;
import com.example.stockchain.StockTransactionInform;
import com.example.stockchain.StockTransactionRecordInform;
import com.example.stockchain.WalletInform;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bcapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        // --LOADING--
        String serverIp = "13.59.189.77";
        Context ctx = getApplicationContext();
        BlockChain bc = new BlockChain(ctx, serverIp);
//        bc.deleteConfig();
        if(!bc.checkCreatedConfig()){
            if(!bc.createConfig()){
                return;
            }
        }

        bc.startNode();

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                //check node connected
                while(bc.checkNodeStarted() == false);
                while(bc.checkNodeSync() == false);

                // --CREATE_ACCOUNT--
                Account ac = new Account(getApplicationContext(), serverIp);
                String username = "helloworld";
                if (ac.isUsernameExistsBlockchain(username)) {
                    Log.d("go", "blockchain account exists");
                } else {
                    if (ac.isUsernameExistsLocal(username)) {
                        Log.d("go", "local account exists");
                    }else{
                        try {
                            String mnemonic = ac.createWalletLocal(username);
                            Log.d("go", "Memonic : "+mnemonic);

                            try {
                                String address = ac.getAddressByUsernameLocal(username);

                                if (ac.walletRegisterBlockchain(username, address)) {
                                    while (!ac.isUsernameExistsBlockchain(username)) ;
                                    Log.d("go", "success");
                                } else {
                                    Log.d("go", "failed register account");
                                }
                            } catch (IOException e) {
                                Log.d("go", "failed get address");
                            }
                        } catch (IOException ioException) {
                            Log.d("go", "failed create local account");
                        }

                    }
                }

                // --LOGIN--
                username = "root";
                String mnemonic = "retreat uphold table initial liquid glow debris carbon salon expire mystery entry blue skirt differ wing general only human scout fish pipe asthma base";
                if (ac.isUsernameExistsBlockchain(username)) {
                    if (ac.isUsernameExistsLocal(username)) {
                        Log.d("go", "local account exists");
                    }else{
                        //address는 mnemonic->pub_key->address로 생성되기에 mnemonic으로
                        if (ac.createWalletByMnemonicLocal(username, mnemonic)) {
                            Log.d("go", "Success login");
                        }else {
                            Log.d("go", "Failed login");
                        }
                    }
                } else {
                    Log.d("go", "blockchain account not exists");
                }

                //-- SHOW WALET --
                try {
                    ArrayList<WalletInform> walletList = ac.getWalletList();
                    Log.d("go", "success show wallet");
                } catch (IOException e) {
                    Log.d("go", "failed show wallet");
                }

                //--STOCK_DATA--
                StockData stockData = new StockData(getApplicationContext());
                try {
                    ArrayList<StockDataInform> stockDataInformList = stockData.getStockDataList();
                    Log.d("go", "success get stock data");
                } catch (IOException e) {
                    Log.d("go", "failed get stock data");
                }

                try {
                    StockDataInform stockDataInform = stockData.getStockData("001520");
                    Log.d("go", "suceess get stock data");
                } catch (IOException e) {
                    Log.d("go", "failed get stock data");
                }

                //--BUY_STOCK_TRANSACTION--
                StockTransaction stockTransaction = new StockTransaction(getApplicationContext());
                try {
                    stockTransaction.createStockTransaction(username, "131400", 10);
                    Log.d("go", "success buy stock");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("go", "failed buy stock");
                }

                try {
                    stockTransaction.createStockTransaction(username, "340120", 11);
                    Log.d("go", "success buy stock");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("go", "failed buy stock");
                }

                //--SHOW_STOCK_TRANSACTION--
                try {
                    String address = ac.getAddressByUsernameLocal(username);
                    ArrayList<StockTransactionInform> stockTransactionInformList = stockTransaction.getStockTransactionInform(address);
                    Log.d("go", "success show stock transaction");
                } catch (IOException e) {
                    Log.d("go", "failed show stock transaction");
                }

                //--SELL_STOCK_TRANSACTION--
                try {
                    stockTransaction.deleteStockTransaction(username, "131400", 9);
                    Log.d("go", "success sell stock transaction");
                } catch (IOException e) {
                    Log.d("go", "failed sell stock transaction");
                }

                //--SHOW_STOCK_TRANSACTION--
                try {
                    String address = ac.getAddressByUsernameLocal(username);
                    ArrayList<StockTransactionInform> stockTransactionInformList = stockTransaction.getStockTransactionInform(address);
                    Log.d("go", "success show stock transaction");
                } catch (IOException e) {
                    Log.d("go", "failed show stock transaction");
                }

                //--SHOW_STOCK_BANK_INFORM--
                try {
                    String address = ac.getAddressByUsernameLocal(username);
                    StockBankInform stockBankInform = stockTransaction.getStockBankeInform(address);
                    Log.d("go", "success show stock bank inform");
                } catch (IOException e) {
                    Log.d("go", "failed show stock bank inform");
                }

                //--SHOW_STOCK_RECORD
                try {
                    String address = ac.getAddressByUsernameLocal(username);
                    ArrayList<StockTransactionRecordInform> stockTransactionRecordInform = stockTransaction.getStockTransactionRecord(address);
                    Log.d("go", "success show stock transaction record");
                } catch (IOException e) {
                    Log.d("go", "failed show stock transaction record");
                }
            }
        }
        Thread thread = new Thread(new NewRunnable());
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}