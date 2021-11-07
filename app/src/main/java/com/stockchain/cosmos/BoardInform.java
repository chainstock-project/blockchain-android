package com.stockchain.cosmos;

public class BoardInform {
    int id;
    String username;
    String title;
    String body;

    public BoardInform(int id, String title, String body, String username) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.username = username;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
