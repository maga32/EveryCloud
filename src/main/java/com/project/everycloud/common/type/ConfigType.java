package com.project.everycloud.common.type;

public enum ConfigType {
    PORT            ("port"),
    OPEN_BROWSER    ("openBrowser");

    String key;

    ConfigType(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }

    public String ecKey() {
        return "ec-" + this.key;
    }

}
