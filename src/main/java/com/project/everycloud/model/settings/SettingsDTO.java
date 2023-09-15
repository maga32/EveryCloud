package com.project.everycloud.model.settings;

public class SettingsDTO {
    private String type;
    private String externalUrl;

    public SettingsDTO() {}

    public SettingsDTO(String type, String externalUrl) {
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
