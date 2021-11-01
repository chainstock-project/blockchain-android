package com.stockchain.bcapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stockchain.cosmos.Account;
import com.stockchain.cosmos.PreferenceManager;
import com.stockchain.cosmos.Tools;

import java.io.IOException;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button goLoginButton = findViewById(R.id.goLoginButton);
        goLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button signinButton  = findViewById(R.id.signinButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView usernameView = findViewById(R.id.signinName);
                String username = usernameView.getText().toString();

                // --siginin--
                Account ac = new Account(getApplicationContext(), getString(R.string.server_ip));
                if (ac.isUsernameExistsBlockchain(username)) {
                    Tools.showDialog(SigninActivity.this, "회원가입", "이미 존재하는 계정입니다.");
                } else {
                    if (ac.isUsernameExistsLocal(username)) {
                        ac.deleteWalletLocal(username);
                    }
                    try {
                        String mnemonic = ac.createWalletLocal(username);
                        try {
                            String address = ac.getAddressByUsernameLocal(username);
                            if (ac.walletRegisterBlockchain(username, address)) {
                                while (!ac.isUsernameExistsBlockchain(username));
                                PreferenceManager pm = new PreferenceManager();
                                pm.setString(getApplicationContext(), "username", username);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Tools.showDialog(SigninActivity.this, "회원가입", "회원가입 실패");
                           }

                        } catch (IOException e) {
                            Tools.showDialog(SigninActivity.this, "회원가입", "계정주소 얻기를 실패했습니다.");
                        }
                    } catch (IOException ioException) {
                        Tools.showDialog(SigninActivity.this, "회원가입", "지갑생성에 실패했습니다.");
                    }
                }
            }
        });
    }
}