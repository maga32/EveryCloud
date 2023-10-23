package com.project.everycloud.service;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

public class InstallService {

    public static String getDbKey() {
        getMac();
        return "";
    }

    public static String getDbValue() {
        getMac();
        return "";
    }

    public void dbInstall() {
        String path = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "EveryCloud2.db";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path+"?"+decryptor(getDbValue()));

            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE SETTINGS (TYPE TEXT, EXTERNAL_URL TEXT" +
                            ", PRIMARY KEY(TYPE))");
            stmt.execute("CREATE TABLE GROUP_SETTING (NO INTEGER, NAME TEXT NOT NULL" +
                            ", PRIMARY KEY(NO))");
            stmt.execute("CREATE TABLE SHARE (LINK TEXT, PATH TEXT NOT NULL UNIQUE, DATE TEXT, METHOD INTEGER NOT NULL, PASS TEXT, AUTH INTEGER NOT NULL DEFAULT 0" +
                            ", PRIMARY KEY(LINK))");
            stmt.execute("CREATE TABLE SHARE_GROUP (SHARE_LINK TEXT NOT NULL, GROUP_NO INTEGER NOT NULL, AUTH INTEGER NOT NULL DEFAULT 0" +
                            ", PRIMARY KEY(SHARE_LINK, GROUP_NO)" +
                            ", FOREIGN KEY(GROUP_NO) REFERENCES GROUP_SETTING(NO)" +
                            ", FOREIGN KEY(SHARE_LINK) REFERENCES SHARE(LINK))");
            stmt.execute("CREATE TABLE USER (ID TEXT, PASS TEXT NOT NULL, NICKNAME TEXT NOT NULL, EMAIL TEXT, AUTH TEXT NOT NULL DEFAULT N, GROUP_NO INTEGER NOT NULL DEFAULT 1" +
                            ", PRIMARY KEY(ID)" +
                            ", FOREIGN KEY(GROUP_NO) REFERENCES GROUP_SETTING(NO))");

            stmt.execute("INSERT INTO SETTINGS (TYPE, EXTERNAL_URL) " +
                            "VALUES('default', 'http://127.0.0.1:11024')");
            stmt.execute("INSERT INTO SETTINGS (TYPE, EXTERNAL_URL) " +
                            "VALUES('admin', 'http://127.0.0.1:11024')");
            stmt.execute("INSERT INTO GROUP_SETTING  (NO, NAME) " +
                            "VALUES(1, 'default')");
            stmt.execute("INSERT INTO USER (ID, PASS, NICKNAME, EMAIL, AUTH, GROUP_NO) " +
                            "VALUES('admin', 'admin', 'admin', null, 'Y', 1)");

            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        System.out.println("install finished");
    }

    public static String getMac() {
        StringBuilder result = new StringBuilder();

        try {
            if(System.getProperty("os.name").toLowerCase().contains("win")) {
                InetAddress ip = InetAddress.getLocalHost();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                byte[] mac = network.getHardwareAddress();

                for (int i = 0; i < mac.length; i++) {
                    result.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                }
            } else {
                Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
                while (networks.hasMoreElements()) {
                    StringBuilder addr = new StringBuilder();
                    NetworkInterface network = networks.nextElement();
                    if(network.getName().equals("en0")) {
                        byte[] mac = network.getHardwareAddress();
                        if (mac != null) {
                            for (int i = 0; i < mac.length; i++) {
                                result.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return result.toString();
    }

    public static StringEncryptor encrytConfig() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(getDbKey());
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

    public String decryptor(String encryptText) {
        return encrytConfig().decrypt(encryptText);
    }
}
