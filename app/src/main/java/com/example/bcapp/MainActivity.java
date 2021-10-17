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
        String serverIp = "3.17.109.211";
        Context ctx = getApplicationContext();
        BlockChain bc = new BlockChain(ctx, serverIp);
        if(!bc.checkInitChainComplete()){
            if(!bc.createConfig()){
                return;
            }
        }
        bc.startNode();
        int a=1;
        class NewRunnable implements Runnable {
            Account ac = new Account(getApplicationContext(), serverIp);
            @Override
            public void run() {
                // --CREATE_ACCOUNT--
                while(bc.checkNodeStarted() == false);

                while(bc.checkNodeSync() == false);

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

                    if (ac.accountRegisterBlockchain(username)) {
                        while (!ac.isUsernameExistsBlockchain(username)) ;
                        Log.d("go", "success");
                    } else {
                        Log.d("go", "failed register account");
                        return;
                    }
                }

                // --LOGIN--
                ac.showKeys();

                if (!bc.checkNodeStarted()) {
                    return;
                }

                if (!bc.checkNodeSync()) {
                    return;
                }

                username = "root";
                String mnemonic = "retreat uphold table initial liquid glow debris carbon salon expire mystery entry blue skirt differ wing general only human scout fish pipe asthma base";
                ac.deleteLocalAccountLocal(username);
                if (ac.isUsernameExistsBlockchain(username)) {
                    if (ac.isUsernameExistsLocal(username)) {
                        Log.d("go", "local account exists");
                    }

                    //address는 mnemonic->pub_key->address로 생성되기에 mnemonic으로
                    if (!ac.createWalletByMnemonicLocal(username, mnemonic)) {
                        return;
                    }
                    String address;
                    try {
                        address = ac.getAddressByUsernameLocal(username);
                    } catch (IOException e) {
                        Log.d("go", "faild get add by username");
                        return;
                    }

                    while (!ac.isAccountExistsBlockchain(address, username)) ;
                    Log.d("go", "Success login");
                } else {
                    Log.d("go", "blockchain account not exists");
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