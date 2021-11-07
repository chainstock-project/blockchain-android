package com.stockchain.cosmos;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Board {
    private Context ctx;
    private final String blockchainPath;
    private final String homeDir;

    public Board(Context ctx) {
        this.ctx = ctx;
        this.blockchainPath = ctx.getApplicationInfo().nativeLibraryDir + "/blockchaind.so";
        this.homeDir = ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";

    }

    private int getBoardCount() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "list-board", "--limit", "1", "--count-total", "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        if(stdOut.readLine() ==null){
            throw new IOException("dosen't exists");
        }else {
            stdOut.readLine();
            stdOut.readLine();
            stdOut.readLine();
            stdOut.readLine();
            stdOut.readLine();
            String line = stdOut.readLine();
            line = stdOut.readLine();
            if (line == null) {
                return 0;
            } else {
                String[] line_split = line.split(" ");
                int count = (int) Double.parseDouble(line_split[line_split.length - 1].replace("\"", ""));
                return count;
            }
        }
    }

    private int getBoardPageCount(int count) throws IOException {
        int pageCount = count/15;
        if (count % 15 > 0) {
            pageCount++;
        }
        return pageCount;
    }

    public int getBoardPageCount() throws IOException {
        int  count = getBoardCount();
        int pageCount = count/15;
        if (count % 15 > 0) {
            pageCount++;
        }
        return pageCount;
    }

    public ArrayList<BoardInform> listBoardPage(int page) throws IOException {
        int boardCount = getBoardCount();
        int pageCount = getBoardPageCount(boardCount);
        if(page > pageCount || page < 1) {
            throw new IOException("invalid page");
        }

        int offset = boardCount - (page*15);
        int limit = 15;
        if(offset<0){
            offset=0;
            limit+=offset;
        }

        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "list-board", "--limit", String.valueOf(limit), "--offset", String.valueOf(offset), "--count-total", "--home", homeDir);
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }else {
            ArrayList<BoardInform> boardInformList = new ArrayList<>();
            Account ac = new Account(ctx);
            ArrayList<AccountInform> userList = ac.getUserList();
            while ((line = stdOut.readLine()) != null) {
                 if(line.equals("pagination:")){
                    break;
                }

                String[] line_split = line.split("body: ");
                String body = line_split[line_split.length - 1].replace("\"","");

                line = stdOut.readLine();
                line_split = line.split(" ");
                String creator = line_split[line_split.length - 1];
                String username = ac.getUsernameByAddress(userList, creator);

                line = stdOut.readLine();
                line_split = line.split(" ");
                int id = (int) Double.parseDouble(line_split[line_split.length - 1].replace("\"",""));

                line = stdOut.readLine();
                line_split = line.split("title: ");
                String title = line_split[line_split.length - 1].replace("\"","");

                BoardInform boardInform = new BoardInform(id, title, body, username);
                boardInformList.add(boardInform);
            }

            Collections.reverse(boardInformList);
            return boardInformList;
        }
    }

    public BoardInform getBoard(String username, int id) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "show-board", String.valueOf(id), "--home", homeDir);
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        } else {
            Account ac = new Account(ctx);
            ArrayList<AccountInform> userList = ac.getUserList();

            line = stdOut.readLine();
            String[] line_split = line.split(" ");
            String body = line_split[line_split.length - 1];

            line = stdOut.readLine();
            line_split = line.split(" ");
            String creator = line_split[line_split.length - 1];

            line = stdOut.readLine();
            line_split = line.split(" ");
            int get_id = (int) Double.parseDouble(line_split[line_split.length - 1].replace("\"",""));

            line = stdOut.readLine();
            line_split = line.split(" ");
            String title = line_split[line_split.length - 1];

            BoardInform boardInform = new BoardInform(get_id, title, body, username);
            return boardInform;
        }
    }

    public String createBoard (String username, String title, String body) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "create-board", title, body, "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "--gas=auto", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        (new BufferedReader(new InputStreamReader(process.getErrorStream()))).readLine();
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException((new BufferedReader(new InputStreamReader(process.getErrorStream()))).readLine());
        }
        else{
            String tx="";
            while(line != null){
                tx = line;
                line = stdOut.readLine();
            }
            return tx.substring(8);
        }
    }

    public String updateBoard(String username, int id, String title, String body) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "update-board", String.valueOf(id), title, body, "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "--gas=auto", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }else{
            String tx="";
            while(line != null){
                tx = line;
                line = stdOut.readLine();
            }
            return tx.substring(8);
        }
    }

    public String deleteBoard(String username, int id)throws IOException{
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "delete-board", String.valueOf(id), "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }else{
            String tx="";
            while(line != null){
                tx = line;
                line = stdOut.readLine();
            }
            return tx.substring(8);
        }
    }

}
