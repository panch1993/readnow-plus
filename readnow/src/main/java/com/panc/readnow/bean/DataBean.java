package com.panc.readnow.bean;

/**
 * Created by Pan's on 2016-8-3.
 */
public class DataBean {
    public String author;
    public String content;
    public String date;
    public String extra;
    public int status;
    public String title;

    @Override
    public String toString() {
        return "DataBean{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", extra='" + extra + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                '}';
    }
}
