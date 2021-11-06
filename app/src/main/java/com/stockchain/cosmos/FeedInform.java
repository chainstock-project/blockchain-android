package com.stockchain.cosmos;

public class FeedInform {
    String subject;
    String summary;
    String url;

    public FeedInform(String subject, String summary, String url) {
        this.subject = subject;
        this.summary = summary;
        this.url = url;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
