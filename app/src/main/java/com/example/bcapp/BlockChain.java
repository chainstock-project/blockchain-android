package com.example.bcapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

class AccountException extends Exception{
    AccountException(String msg){
        super(msg);
    }

}


public class BlockChain {
    private Context ctx;
    private ApplicationInfo ai;
    private Intent intent;
    private String blockchainPath;
    private String homeDir;
    private String serverIp;

    BlockChain(Context ctx, String serverIp) {
        this.ctx = ctx;
        this.ai = ctx.getApplicationInfo();
        this.blockchainPath = this.ai.nativeLibraryDir + "/blockchaind.so";
        this.homeDir = this.ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";
        this.serverIp = serverIp;
    }

    private void deleteFolder(String path) {
        File folder = new File(path);
        if (folder.exists()) {
            File[] folder_list = folder.listFiles(); //파일리스트 얻어오기
            for (int i = 0; i < folder_list.length; i++) {
                if (folder_list[i].isFile()) {
                    folder_list[i].delete();
                } else {
                    deleteFolder(folder_list[i].getPath()); //재귀함수호출
                }
                folder_list[i].delete();
            }
            folder.delete(); //폴더 삭제
        }
    }
    public int getLatestHeight() {
        LatestHeight latestHeight = new LatestHeight();
        try {
            latestHeight.start();
            latestHeight.join();
            return Integer.parseInt(latestHeight.getLatestHeight());
        } catch (InterruptedException e) {
            return Integer.parseInt(latestHeight.getLatestHeight());
        }
    }

    public int getDownloadedHeight() {
        DownloadedHeight downloadedHeight = new DownloadedHeight();
        downloadedHeight.start();
        try {
            downloadedHeight.join();
            return Integer.parseInt(downloadedHeight.getDownloadedHeight());
        } catch (InterruptedException e) {
            return Integer.parseInt(downloadedHeight.getDownloadedHeight());
        }
    }

    public int getRemainHeight(){
        return this.getLatestHeight() - this.getDownloadedHeight();
    }

    private void createHome() throws IOException {

        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "init", "second-node", "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String str;
        while ((str = stdOut.readLine()) != null) {
            Log.d("blockchaind", str);
        }
        stdOut.close();
    }

    private void copyGenesisFile() throws IOException {
        //기존genesis파일제거
        String genesisPath = homeDir + "/config/genesis.json";
        File genesisFile = new File(genesisPath);
        if (genesisFile.exists()) {
            genesisFile.delete();
        }

        //asset에 있는 genesis파일 옮기
        AssetManager am = this.ctx.getAssets();
        InputStream in = am.open("genesis.json");
        OutputStream out = new FileOutputStream(genesisPath);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void editConfigToml() throws IOException {
        String configToml = homeDir + "/config/config.toml";
        String nodeId = "ee0e96e8eafbe604b2ca73c93bd5ae3ab43970c1";
        String peer = nodeId + "@" + this.serverIp + ":26656";

        //config.toml 읽기
        BufferedReader br = new BufferedReader(new FileReader(configToml));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }

        //config.toml 수정
        FileWriter fw = new FileWriter(configToml, false); // 파일이 있을경우 덮어쓰기
        String contentReplaced = content.toString().replace("persistent_peers = \"\"", "persistent_peers = \"" + peer + "\"");
        fw.write(contentReplaced);

        br.close();
        fw.close();
    }

    public boolean checkInitChainComplete() {
        File file = new File(this.homeDir);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createConfig() {
        Log.d("go", "----Init Chain----");
        try {
            createHome();
            copyGenesisFile();
            editConfigToml();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void deleteConfig() {
        deleteFolder(homeDir);
    }

    public void startNode() {
        this.intent = new Intent(ctx, BlockChainService.class); // 이동할 컴포넌트
        ComponentName asd = ctx.startService(intent);
    }

    public void stopNode() {
        ctx.stopService(intent);
    }

    public boolean checkNodeStarted(){
        try {
            (new Socket("127.0.0.1", 26657)).close();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public boolean checkNodeSync(){
        if(this.getRemainHeight() > 5) {
            return false;
        }
        return true;
    }

    class LatestHeight extends Thread{
        private String latestHeight;

        public void setLatestHeight() {
            HttpURLConnection urlConn = null;
            try {
                URL url = new URL("http://127.0.0.1:26657/dump_consensus_state");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Accept", "application/json");

                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                    StringBuffer reciveString = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        reciveString.append(line);
                    }
                    JSONObject jsonObj = new JSONObject(reciveString.toString());
                    this.latestHeight = (String) jsonObj.getJSONObject("result").getJSONArray("peers").getJSONObject(0).getJSONObject("peer_state").getJSONObject("round_state").get("height");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
        }

        public String getLatestHeight() {
            return latestHeight;
        }

        public void run(){
            setLatestHeight();
        }
    }

    class DownloadedHeight extends Thread{
        String downloadedHeight;

        private void setDownloadedHeight() {
            HttpURLConnection urlConn = null;
            try {
                URL url = new URL("http://127.0.0.1:26657/abci_info");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Accept", "application/json");

                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                    StringBuffer reciveString = new StringBuffer();
                    String line;
                    // 라인을 받아와 합친다.
                    while ((line = reader.readLine()) != null) {
                        reciveString.append(line);
                    }
                    JSONObject jsonObj = new JSONObject(reciveString.toString());
                    this.downloadedHeight = (String) jsonObj.getJSONObject("result").getJSONObject("response").get("last_block_height");
                }
            } catch (Exception e) { // for openConnection().
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
        }

        public String getDownloadedHeight() {
            return downloadedHeight;
        }

        public void run(){
            this.setDownloadedHeight();
        }
    }
}

class Account{
    private String serverIp;
    private String blockchainPath;
    private String homeDir;

    Account(Context ctx, String serverIp) {
        this.blockchainPath = ctx.getApplicationInfo().nativeLibraryDir + "/blockchaind.so";
        this.homeDir = ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";
        this.serverIp = serverIp;
    }

    public String createWalletLocal(String username) throws IOException {
        Log.d("go","create wallet");
        this.deleteLocalAccountLocal(username);
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

    public void deleteLocalAccountLocal(String username){
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

//    public String getUsernameByAddressLocal(String address) throws IOException {
//        return null;
//    }

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

    public void showKeys(){
        try {
            ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "keys", "list", "--keyring-backend", "test", "--home", homeDir);
            Process process = builder.start();

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line=stdOut.readLine())!=null){
                Log.d("go", line);
            }
        } catch (IOException e) {
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

    //pub_key없을시 error
    public boolean isAddressExistsBlockchain(String address) {
        try {
            ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "account", address, "--home", homeDir);
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

    public boolean isAccountExistsBlockchain(String address, String username){
        return isUsernameExistsBlockchain(username) && isAddressExistsBlockchain(address);
    }

    public boolean accountRegisterBlockchain(String address, String username) {
        AccountRegister ra = new AccountRegister(this.serverIp, address, username);
        try {
            ra.start();
            ra.join();
            return ra.getStatus();
        } catch (InterruptedException e) {
            return ra.getStatus();
        }
    }

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
}