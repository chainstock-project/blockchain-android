package com.example.bcapp;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class test extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        // --CREATE_ACCOUNT--
        class NewRunnable implements Runnable {
            Account ac = new Account(getApplicationContext(), serverIp);
            @Override
            public void run() {
                if(!bc.checkNodeStarted()){
                    return;
                }

                if(!bc.checkNodeSync()){
                    return;
                }

                String username = "hello123114";
                if (ac.isUsernameExistsBlockchain(username)) {
                    System.out.println("blockchain account exists");
                } else {
                    if (ac.isUsernameExistsLocal(username)) {
                        System.out.println("local account exists");
                    }

                    String mnemonic = null;
                    String address = null;
                    try {
                        mnemonic = ac.createWalletLocal(username);
                        address = ac.getAddressByUsernameLocal(username);
                    } catch (IOException ioException) {
                        System.out.println("failed create local account");
                        return;
                    }

                    if (ac.accountRegisterBlockchain(address, username)) {
                        while (!ac.isAccountExistsBlockchain(address, username));
                        System.out.println("success");
                    } else {
                        System.out.println("failed register account");
                        return;
                    }
                }
            }
        }
        Thread thread = new Thread(new NewRunnable());
        thread.start();

        // --LOGIN--
        class NewRunnable2 implements Runnable {
            Account ac = new Account(getApplicationContext(), serverIp);
            @Override
            public void run() {
                ac.showKeys();

                if(!bc.checkNodeStarted()){
                    return;
                }

                if(!bc.checkNodeSync()){
                    return;
                }

                String username = "root";
                String mnemonic = "retreat uphold table initial liquid glow debris carbon salon expire mystery entry blue skirt differ wing general only human scout fish pipe asthma base";
                ac.deleteLocalAccountLocal(username);
                if (ac.isUsernameExistsBlockchain(username)) {
                    if (ac.isUsernameExistsLocal(username)) {
                        System.out.println("local account exists");
                    }

                    //address는 mnemonic->pub_key->address로 생성되기에 mnemonic으로
                    if (!ac.createWalletByMnemonicLocal(username, mnemonic)) {
                        return;
                    }
                    String address;
                    try {
                        address = ac.getAddressByUsernameLocal(username);
                    } catch (IOException e) {
                        System.out.println("faild get add by username");
                        return;
                    }

                    while (!ac.isAccountExistsBlockchain(address, username));
                    System.out.println("Success login");
                }
                else {
                    System.out.println("blockchain account not exists");
                }
            }
        }
        Thread thread2 = new Thread(new NewRunnable2());
        thread2.start();
    }
}