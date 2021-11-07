package com.stockchain.cosmos;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BlockChain {
    private Context ctx;
    private ApplicationInfo ai;
    private Intent intent;
    private String blockchainPath;
    private String homeDir;
    private String serverIp;

    public BlockChain(Context ctx, String serverIp) {
        this.ctx = ctx;
        this.ai = ctx.getApplicationInfo();
        this.blockchainPath = this.ai.nativeLibraryDir + "/blockchaind.so";
        this.homeDir = this.ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";
        this.serverIp = serverIp;
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

    private void setGenesisFile() throws IOException, InterruptedException {
        class GetGenesisObject extends Thread{
            JSONObject genesisJSONObject;
            private void setGenesisJsonObject() {
                HttpURLConnection urlConn = null;
                try {
                    URL url = new URL("http://"+serverIp+":26657/genesis");
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setRequestMethod("GET");

                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                        StringBuffer reciveString = new StringBuffer();
                        String line;
                        // 라인을 받아와 합친다.
                        while ((line = reader.readLine()) != null) {
                            reciveString.append(line);
                        }

                        JSONObject jsonObj = new JSONObject(reciveString.toString());
                        genesisJSONObject = jsonObj.getJSONObject("result").getJSONObject("genesis");
                    }
                } catch (Exception e) { // for openConnection().
                } finally {
                    if (urlConn != null)
                        urlConn.disconnect();
                }
            }

            public void run(){
                this.setGenesisJsonObject();
            }
        }

        //main
        GetGenesisObject getGenesisObject = new GetGenesisObject();
        getGenesisObject.start();
        getGenesisObject.join();

        //기존genesis파일제거
        String genesisPath = homeDir + "/config/genesis.json";
        File genesisFile = new File(genesisPath);
        if (genesisFile.exists()) {
            genesisFile.delete();
        }

        //asset에 있는 genesis파일 옮기
        try (FileWriter fw = new FileWriter(genesisPath)) {
            fw.write(getGenesisObject.genesisJSONObject.toString());
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setNodeConnect() throws IOException, InterruptedException {
        class GetNodeId extends Thread{
            String nodeId;
            private void setNodeId() {
                HttpURLConnection urlConn = null;
                try {
                    URL url = new URL("http://"+serverIp+":26657/status");
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setRequestMethod("GET");

                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                        StringBuffer reciveString = new StringBuffer();
                        String line;
                        // 라인을 받아와 합친다.
                        while ((line = reader.readLine()) != null) {
                            reciveString.append(line);
                        }

                        JSONObject jsonObj = new JSONObject(reciveString.toString());
                        nodeId = jsonObj.getJSONObject("result").getJSONObject("node_info").getString("id");
                    }
                } catch (Exception e) { // for openConnection().
                } finally {
                    if (urlConn != null)
                        urlConn.disconnect();
                }
            }
            public void run(){
                this.setNodeId();
            }
        }
        //main
        GetNodeId getNodeId = new GetNodeId();
        getNodeId.start();
        getNodeId.join();

        String configToml = homeDir + "/config/config.toml";
        String nodeId = getNodeId.nodeId;
        String peer = nodeId + "@" + this.serverIp + ":26656";

        //config.toml 읽기
        BufferedReader br = new BufferedReader(new FileReader(configToml));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }
        br.close();

        //config.toml 수정
        FileWriter fw = new FileWriter(configToml, false); // 파일이 있을경우 덮어쓰기
        String contentReplaced = content.toString().replace("persistent_peers = \"\"", "persistent_peers = \"" + peer + "\"");
        fw.write(contentReplaced);

        fw.close();
    }

    public boolean createConfig() {
        Log.d("go", "----Init Chain----");
        try {
            createHome();
            setGenesisFile();
            setNodeConnect();
            return true;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void deleteConfig() {
        class DeleteFolder{
            void deleteFolder(String path) {
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
        }
        //main
        new DeleteFolder().deleteFolder(homeDir);
    }

    public boolean checkCreatedConfig() {
        File file = new File(this.homeDir);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void startNode() {
        this.intent = new Intent(ctx, BlockChainService.class); // 이동할 컴포넌트
        ComponentName asd = ctx.startService(intent);
    }

    public boolean checkNodeStarted(){
        class CheckNodeStated extends Thread{
            private boolean check = false;

            public void setCheckNodeStated() {
                HttpURLConnection urlConn = null;
                try {
                    URL url = new URL("http://127.0.0.1:26657/");
                    urlConn = (HttpURLConnection) url.openConnection();
                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        this.check = true;
                    }
                } catch (IOException e) {
                    this.check = false;
                } finally {
                    if (urlConn != null)
                        urlConn.disconnect();
                }
            }

            public boolean getCheck() {
                return this.check;
            }

            public void run(){
                setCheckNodeStated();
            }
        }

        CheckNodeStated checkNodeStated = new CheckNodeStated();
        checkNodeStated.start();
        try {
            checkNodeStated.join();
            return checkNodeStated.getCheck();
        } catch (InterruptedException e) {
            return checkNodeStated.getCheck();
        }
    }

    public void stopNode() {
        ctx.stopService(intent);
    }

    public double getLatestHeight() {
        class LatestHeight extends Thread{
            private double latestHeight=Double.POSITIVE_INFINITY;

            public void setLatestHeight() {
                HttpURLConnection urlConn = null;
                try {
                    URL url = new URL("http://127.0.0.1:26657/dump_consensus_state?");
                    urlConn = (HttpURLConnection) url.openConnection();

                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                        StringBuffer reciveString = new StringBuffer();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            reciveString.append(line);
                        }
                        JSONObject jsonObj = new JSONObject(reciveString.toString());
                        int height = Integer.parseInt((String) jsonObj.getJSONObject("result").getJSONArray("peers").getJSONObject(0).getJSONObject("peer_state").getJSONObject("round_state").get("height"));

                        if(height != 0){
                            this.latestHeight = height;
                        }
                    }
                } catch (IOException e) {
                } catch (JSONException e) {
                } finally {
                    if (urlConn != null)
                        urlConn.disconnect();
                }
            }

            public double getLatestHeight() {
                return this.latestHeight;
            }

            public void run(){
                setLatestHeight();
            }
        }

        //main
        LatestHeight latestHeight = new LatestHeight();
        try {
            latestHeight.start();
            latestHeight.join();
            return latestHeight.getLatestHeight();
        } catch (InterruptedException e) {
            return latestHeight.getLatestHeight();
        }
    }

    public int getDownloadedHeight() {
        class DownloadedHeight extends Thread{
            private int downloadedHeight=0;

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
                        this.downloadedHeight = Integer.parseInt((String) jsonObj.getJSONObject("result").getJSONObject("response").get("last_block_height"));
                    }
                } catch (Exception e) { // for openConnection().
                } finally {
                    if (urlConn != null)
                        urlConn.disconnect();
                }
            }

            public int getDownloadedHeight() {
                return downloadedHeight;
            }

            public void run(){
                this.setDownloadedHeight();
            }
        }

        //main
        DownloadedHeight downloadedHeight = new DownloadedHeight();
        downloadedHeight.start();
        try {
            downloadedHeight.join();
            return downloadedHeight.getDownloadedHeight();
        } catch (InterruptedException e) {
            return downloadedHeight.getDownloadedHeight();
        }
    }

    public double getRemainHeight(){

        return this.getLatestHeight() - this.getDownloadedHeight();
    }

    public boolean checkNodeSync(){
        double height = this.getRemainHeight();

        if(height <= 2) {
            return false;
        }
        return true;
    }

    public boolean checkTxCommitted(String tx){
        try {
            ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "tx", tx, "--home", homeDir);
            Process process = builder.start();

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if(stdOut.readLine() == null){
                return false;
            }
            else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
}