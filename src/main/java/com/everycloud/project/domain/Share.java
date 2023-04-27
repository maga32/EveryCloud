package com.everycloud.project.domain;

import java.sql.Timestamp;

public class Share {
    private String link;
    private String path;
    private Timestamp date;
    private Integer method;
    private String pass;
    private Integer auth;

    public Share() {}

    public Share(String link, String path, Timestamp date, Integer method, String pass, Integer auth) {
        this.link = link;
        this.path = path;
        this.date = date;
        this.method = method;
        this.pass = pass;
        this.auth = auth;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "Share{" +
                "link='" + link + '\'' +
                ", path='" + path + '\'' +
                ", date='" + date + '\'' +
                ", method=" + method +
                ", pass='" + pass + '\'' +
                ", auth=" + auth +
                '}';
    }
}
