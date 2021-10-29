package com.example.stockchain;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Account {
    private Context ctx;
    private String serverIp;
    private String blockchainPath;
    private String homeDir;

    public Account(Context ctx, String serverIp) {
        this.ctx = ctx;
        this.blockchainPath = ctx.getApplicationInfo().nativeLibraryDir + "/blockchaind.so";
        this.homeDir = ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";
        this.serverIp = serverIp;
    }

    public ArrayList<WalletInform> getWalletList() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "keys", "list", "--keyring-backend", "test", "--home", homeDir);
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        ArrayList<WalletInform> walletInformList = new ArrayList<>();
        while((line=stdOut.readLine())!=null){
            if(line.equals("")) break;
            String[] line_split = line.split(" ");
            String username = line_split[line_split.length - 1];

            String type  = stdOut.readLine();

            line = stdOut.readLine();
            line_split = line.split(" ");
            String address = line_split[line_split.length - 1];

            String pubKey = stdOut.readLine();
            String mnemonic = stdOut.readLine();
            String threshold = stdOut.readLine();
            String pubkeys = stdOut.readLine();

            WalletInform walletInform = new WalletInform(username, address);
            walletInformList.add(walletInform);
        }

        return walletInformList;
    }

    public String createWalletLocal(String username) throws IOException {
        Log.d("go","create wallet");
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "keys", "add", username, "--keyring-backend", "test", "--home", homeDir);

        Process process = builder.start();

        String mnemonic = null;
        BufferedReader stdErrOut = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = stdErrOut.readLine();
        while(line != null){
            mnemonic = line;
            line=stdErrOut.readLine();
        }

        stdErrOut.close();
        return mnemonic;
    }
    public void deleteWalletLocal(String username){
        try {
            ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "keys", "delete", username, "-y", "--keyring-backend", "test", "--home", homeDir);
            Process process = builder.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean walletRegisterBlockchain(String username, String address) {
        class AccountRegister extends Thread{
            private boolean status;
            private String serverIp;
            private String address;
            private String username;


            AccountRegister(String serverIp, String address, String username){
                this.serverIp = serverIp;
                this.status = false;
                this.address = address;
                this.username = username;
            }

            private void accountRegister() {
                HttpURLConnection urlConn = null;
                try {
                    String server = "http://"+this.serverIp+"/register";
                    String parameter = "?address="+this.address+"&name="+this.username;
                    URL url = new URL(server+parameter);

                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setRequestMethod("GET");
                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        this.status=true;
                    }
                } catch (Exception e) { // for openConnection().
                    e.printStackTrace();
                } finally {
                    if (urlConn != null)
                        urlConn.disconnect();
                }
            }

            public boolean getStatus() {
                return this.status;
            }

            public void run(){
                this.accountRegister();
            }
        }

        AccountRegister ra = new AccountRegister(this.serverIp, address, username);
        try {
            ra.start();
            ra.join();
            return ra.getStatus();
        } catch (InterruptedException e) {
            return ra.getStatus();
        }
    }
    public boolean createWalletByMnemonicLocal(String username, String mnemonic){
        try {
            String cmd = "echo \""+mnemonic+"\" | "+this.blockchainPath+" keys add "+username+" --recover --keyring-backend test --home "+homeDir;
            ProcessBuilder builder = new ProcessBuilder("/system/bin/sh", "-c", cmd);
            Process process = builder.start();
            BufferedReader stdErrOut = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while((line=stdErrOut.readLine())!=null){
                Log.d("go", line);
            }
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while((line=stdOut.readLine())!=null){
                Log.d("go", line);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isUsernameExistsLocal(String username){
        try {
            ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "keys", "show", username, "--keyring-backend", "test", "--home", homeDir);
            Process process = builder.start();

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = stdOut.readLine();
            if (line == null) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
    public boolean isUsernameExistsBlockchain(String username){
        try {
            ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "show-user", username, "--home", homeDir);
            Process process = builder.start();

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = stdOut.readLine();
            if (line == null) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
    public boolean isAddressExistsBlockchain(String address) {
        try {
            ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "account", address, "--home", homeDir);
            Process process = builder.start();

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = stdOut.readLine();
            if (line == null) {
                //pub_key없을시 error
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
    public String getAddressByUsernameLocal(String username) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "keys", "show", username, "-a", "--keyring-backend", "test", "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if(line==null){
            throw new IOException("local account not exists");
        }
        return line;
    }

}
