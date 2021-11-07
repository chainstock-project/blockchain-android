package com.stockchain.bcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stockchain.cosmos.Account;
import com.stockchain.cosmos.PreferenceManager;
import com.stockchain.cosmos.Tools;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button goSigninButton = findViewById(R.id.goSigninButton);
        goSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView usernameView = findViewById(R.id.signinName);
                TextView mnemonicView = findViewById(R.id.mnemonic);
                String username = usernameView.getText().toString();
                String mnemonic = mnemonicView.getText().toString();

                // --LOGIN--
                Account ac = new Account(getApplicationContext(), getString(R.string.server_ip));
//                ac.createWalletByMnemonicLocal(username, mnemonic);
                if (ac.checkAccountRegistered(username)) {
                    if(ac.checkAccountCreated(username)){
                        ac.deleteAccount(username);
                    }

                    if(ac.createAccountByMnemonic(username, mnemonic)){
                        if (ac.checkCreatedAccountEqualRegisteredAccountByUsername(username)){
                            try {
                                String address = ac.getAddressByUsernameLocal(username);
                                PreferenceManager pm = new PreferenceManager();
                                pm.setString(getApplicationContext(), "username", username);
                                pm.setString(getApplicationContext(), "address", address);
                            } catch (IOException e) {
                                finishAffinity();
                                System.exit(0);
                            }
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Tools.showDialog(LoginActivity.this, "로그인", "해당 계정을 등록할때 사용된 mnemonic이 아닙니다.");
                            ac.deleteAccount(username);
                        }
                    }else{
                        Tools.showDialog(LoginActivity.this, "로그인", "유효한 mnemonic이 아닙니다.");
                        return;
                    }

                } else {
                    Tools.showDialog(LoginActivity.this, "로그인", "등록되지않은 Name입니다.");
                }
            }
        });
    }
}