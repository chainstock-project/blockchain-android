package com.example.bcapp;

import android.content.Context;
import android.os.Bundle;

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
        if(!bc.checkInitChainComplete()){
            if(!bc.createConfig()){
                return;
            }
        }

        bc.startNode();

        class NewRunnable implements Runnable {
            Account ac = new Account(getApplicationContext(), serverIp);
            Stock stock = new Stock(getApplicationContext(), serverIp);
            @Override
            public void run() {
                // --CREATE_ACCOUNT--
                while(bc.checkNodeStarted() == false);
                while(bc.checkNodeSync() == false);

                ArrayList<StockData> stockDataList = null;
                try {
                    stockDataList = stock.getStockDataList();
                } catch (IOException e) {
                    Log.d("go", "failed get stock data");
                }
                StockData stockData = null;
                try {
                    stockData = stock.getStockData("001520");
                } catch (IOException e) {
                    Log.d("go", "failed get stock data");
                }


                String username = "hello123114";
                if (ac.isUsernameExistsBlockchain(username)) {
                    Log.d("go", "blockchain account exists");
                } else {
                    if (ac.isUsernameExistsLocal(username)) {
                        Log.d("go", "local account exists");
                    }else{
                        try {
                            String mnemonic = ac.createWalletLocal(username);
                            Log.d("go", "Memonic : "+mnemonic);
                        } catch (IOException ioException) {
                            Log.d("go", "failed create local account");
                            return;
                        }
                    }
                    String address = null;
                    try {
                        address = ac.getAddressByUsernameLocal(username);
                    } catch (IOException e) {
                        Log.d("go", "failed get address");
                        return;
                    }
                    if (ac.accountRegisterBlockchain(address, username)) {
                        while (!ac.isUsernameExistsBlockchain(username)) ;
                        Log.d("go", "success");
                    } else {
                        Log.d("go", "failed register account");
                        return;
                    }
                }



                // --LOGIN--
                while(bc.checkNodeStarted() == false);
                while(bc.checkNodeSync() == false);

                ac.showKeys();
                username = "root";
                String mnemonic = "retreat uphold table initial liquid glow debris carbon salon expire mystery entry blue skirt differ wing general only human scout fish pipe asthma base";
                ac.deleteLocalAccountLocal(username);
                if (ac.isUsernameExistsBlockchain(username)) {
                    if (ac.isUsernameExistsLocal(username)) {
                        Log.d("go", "local account exists");
                    }else{
                        //address는 mnemonic->pub_key->address로 생성되기에 mnemonic으로
                        if (!ac.createWalletByMnemonicLocal(username, mnemonic)) {
                            return;
                        }
                        Log.d("go", "Success login");
                    }
                } else {
                    Log.d("go", "blockchain account not exists");
                }

                //--BUY_STOCK--
                try {
                    stock.createStockTransaction(username, "034730", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try{
                    String address = ac.getAddressByUsernameLocal(username);
                    ArrayList<HoldingStock> holdingStockList = stock.getStockTransaction(address);
                    System.out.println("asd");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //--SELL_STOCK--
                try {
                    stock.deleteStockTransaction(username, "034730", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try{
                    String address = ac.getAddressByUsernameLocal(username);
                    ArrayList<HoldingStock> holdingStockList = stock.getStockTransaction(address);
                } catch (IOException e) {
                    e.printStackTrace();
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