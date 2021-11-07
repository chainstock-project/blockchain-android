package com.stockchain.bcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stockchain.cosmos.Account;
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
                if (ac.checkAccountRegistered(username)) {
                    Tools.showDialog(SigninActivity.this, "회원가입", "이미 존재하는 계정입니다.");
                } else {
                    if (ac.checkAccountCreated(username)) {
                        ac.deleteAccount(username);
                    }
                    try {
                        String mnemonic = ac.createAccount(username);
                        String address = ac.getAddressByUsernameLocal(username);
                        if (ac.accountRegister(username, address)) {
                            while (!ac.checkAccountRegistered(username));
                            String messege = "mnemonic : " + mnemonic + "\n\nmnemonic은 로그인시 필요니다. 기록해두십시오.";
                            Tools.showDialog(SigninActivity.this, "회원가입성공", messege);
                        } else {
                            Tools.showDialog(SigninActivity.this, "회원가입", "블록체인 등록에 실패하셨습니다.");
                       }
                    } catch (IOException ioException) {
                        Tools.showDialog(SigninActivity.this, "회원가입", "지갑생성에 실패했습니다.");
                    }
                }
            }
        });
    }
}