package com.everycloud.project.domain;

public class Settings {
    private String type;
    private String externalUrl;

    public Settings() {}

    public Settings(String type, String externalUrl) {
        this.type = type;
        this.externalUrl = externalUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "type='" + type + '\'' +
                ", externalUrl='" + externalUrl + '\'' +
                '}';
    }
}
