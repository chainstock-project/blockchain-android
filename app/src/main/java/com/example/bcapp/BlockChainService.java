package com.example.bcapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BlockChainService extends Service {
    Node node;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        String blockchainPath = getApplicationInfo().nativeLibraryDir + "/blockchaind.so";
        String homeDir = getApplicationContext().getFilesDir().getAbsolutePath() + "/.blockchaind";
        this.node = new Node(blockchainPath, homeDir);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.node.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.node!=null) {
            this.node.interrupt();
        }
    }
}

class Node extends Thread{
    private String blockchainPath;
    private String homeDir;
    private Process process;

    Node(String blockchainPath, String homeDir){
        this.blockchainPath = blockchainPath;
        this.homeDir = homeDir;
    }

    private void startNode() throws IOException {
        Log.d("go", "----Start Node----");
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "start", "second-node", "--home", homeDir);
        this.process = builder.start();

        BufferedReader stdOutError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String str;
        while ((str = stdOutError.readLine()) != null) {
            if(Thread.currentThread().isInterrupted()) {
                return;
            }
            Log.d("blockchaind", str);
        }
    }

    public void run(){
        try {
            while(!Thread.currentThread().isInterrupted()) {
                startNode();
            }
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}