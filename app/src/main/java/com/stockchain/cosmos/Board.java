package com.stockchain.cosmos;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Board {
    private final String blockchainPath;
    private final String homeDir;

    public Board(Context ctx) {
        this.blockchainPath = ctx.getApplicationInfo().nativeLibraryDir + "/blockchaind.so";
        this.homeDir = ctx.getFilesDir().getAbsolutePath() + "/.blockchaind";

    }

    public int getBoardCount() throws IOException {
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

    public int getBoardPageCount(int count) throws IOException {
        int pageCount = count/15;
        if (count % 15 > 0) {
            pageCount++;
        }
        return pageCount;
    }

    public ArrayList<BoardInform> listBoardPage(int count, int page) throws IOException {
        int pageCount = getBoardPageCount(count);
        if(page > pageCount || page < 1) {
            throw new IOException("invalid page");
        }

        int offset = (page-1)*15;
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "query", "blockchain", "list-board", "--page", String.valueOf(page), "--limit", "15", "--offset", String.valueOf(offset), "--count-total", "--home", homeDir);
        Process process = builder.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }else {
            ArrayList<BoardInform> boardInformList = new ArrayList<>();
            while ((line = stdOut.readLine()) != null) {
                if(line.equals("pagination:")){
                    stdOut.readLine();
                    line = stdOut.readLine();
                    String[] line_split = line.split(" ");
                    int new_count = (int) Double.parseDouble(line_split[line_split.length - 1].replace("\"",""));

                    if (new_count != count){
                        throw new IOException("count diffrent");
                    }
                    break;
                }

                String[] line_split = line.split(" ");
                String body = line_split[line_split.length - 1];

                String creator = stdOut.readLine();

                line = stdOut.readLine();
                line_split = line.split(" ");
                int id = (int) Double.parseDouble(line_split[line_split.length - 1].replace("\"",""));

                line = stdOut.readLine();
                line_split = line.split(" ");
                String title = line_split[line_split.length - 1];

                BoardInform boardInform = new BoardInform(id, title, body);
                boardInformList.add(boardInform);
            }

            return boardInformList;
        }
    }

    public void createBoard (String username, String title, String body) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "create-board", title, body, "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "--gas=auto", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        (new BufferedReader(new InputStreamReader(process.getErrorStream()))).readLine();
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException((new BufferedReader(new InputStreamReader(process.getErrorStream()))).readLine());
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
            line = stdOut.readLine();
            String[] line_split = line.split(" ");
            String body = line_split[line_split.length - 1];

            String creator = stdOut.readLine();

            line = stdOut.readLine();
            line_split = line.split(" ");
            int get_id = (int) Double.parseDouble(line_split[line_split.length - 1].replace("\"",""));

            line = stdOut.readLine();
            line_split = line.split(" ");
            String title = line_split[line_split.length - 1];

            BoardInform boardInform = new BoardInform(get_id, title, body);
            return boardInform;
        }
    }

    public void updateBoard(String username, int id, String title, String body) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "update-board", String.valueOf(id), title, body, "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "--gas=auto", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }
    }

    public void deleteBoard(String username, int id)throws IOException{
        ProcessBuilder builder = new ProcessBuilder(this.blockchainPath, "tx", "blockchain", "delete-board", String.valueOf(id), "--from", username, "--keyring-backend", "test", "--home", homeDir, "--chain-id", "stock-chain", "-y");
        Process process = builder.start();
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = stdOut.readLine();
        if (line == null) {
            throw new IOException("dosen't exists");
        }
    }

}
