package com.project.everycloud.common.config;

import com.project.everycloud.EveryCloudApplication;
import com.project.everycloud.common.type.ConfigType;
import com.project.everycloud.service.InstallService;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StarterConfig {

    private static String 	RESOURCE 				= "/datasource/";
    private static String 	DB 						= "EveryCloud.db";
    private static String 	CONFIG 					= "config.yml";
    private static String 	CONFIG_DEFAULT 			= "config-default.yml";
    private static String 	FAVICON 	        	= "favicon.ico";
    private static String 	PID_FILE 	        	= "EveryCloud.pid";
    private static Path     LOCAL_FOLDER 			= Paths.get(System.getProperty("user.home") + File.separator + ".everyCloud");
    private static Path 	LOCAL_DB 				= Paths.get(LOCAL_FOLDER + File.separator + DB);
    private static Path 	LOCAL_CONFIG 			= Paths.get(LOCAL_FOLDER + File.separator + CONFIG);
    private static Path 	LOCAL_CONFIG_DEFAULT 	= Paths.get(LOCAL_FOLDER + File.separator + CONFIG_DEFAULT);
    private static Path 	LOCAL_FAVICON           = Paths.get(LOCAL_FOLDER + File.separator + FAVICON);
    private static Path 	LOCAL_PID_FILE          = Paths.get(LOCAL_FOLDER + File.separator + PID_FILE);
    private static String 	OS 						= System.getProperty("os.name").toLowerCase();

    public static void init() {
        if(!Files.exists(LOCAL_FOLDER)) new File(LOCAL_FOLDER.toString()).mkdir();

        if(!Files.exists(LOCAL_DB)) 		copyFiles(RESOURCE+DB, 		LOCAL_DB.toString());
        if(!Files.exists(LOCAL_CONFIG)) 	copyFiles(RESOURCE+CONFIG, 	LOCAL_CONFIG.toString());
        if(!Files.exists(LOCAL_FAVICON)) 	copyFiles(RESOURCE+FAVICON, LOCAL_FAVICON.toString());
        copyFiles(RESOURCE+CONFIG, 	LOCAL_CONFIG_DEFAULT.toString());

        try {
            Yaml yaml = new Yaml();
            Reader configFile = new FileReader(String.valueOf(LOCAL_CONFIG));
            Map<String, Object> yamlData = yaml.load(configFile);

            Reader defaultConfigFile = new FileReader(String.valueOf(LOCAL_CONFIG_DEFAULT));
            Map<String, Object> defaultYamlData = yaml.load(defaultConfigFile);

            /*---------- if config.yml file is invalid, fix it ----------*/
            boolean configError = false;
            int intTest;
            boolean booleanTest;
            String stringTest;

            if(yamlData == null) {
                yamlData = new HashMap<String, Object>();
                configError = true;
            }

            // get port
            try { intTest = (int) yamlData.get(ConfigType.PORT.ecKey());
            } catch (Exception e) {
                yamlData.put(ConfigType.PORT.ecKey(), defaultYamlData.get(ConfigType.PORT.key()));
                configError = true;
            }
            // get open browser property
            try { booleanTest = (Boolean) yamlData.get(ConfigType.OPEN_BROWSER.ecKey());
            } catch (Exception e) {
                yamlData.put(ConfigType.OPEN_BROWSER.ecKey(), defaultYamlData.get(ConfigType.OPEN_BROWSER.key()));
                configError = true;
            }

            // get db cert
            if(yamlData.get(ConfigType.DB.ecKey()) == null) {
                yamlData.put(ConfigType.DB.ecKey(), "ENC("+InstallService.getDbValue()+")");
                configError = true;
            }

            yamlData.remove(ConfigType.PORT.key());
            yamlData.remove(ConfigType.OPEN_BROWSER.key());
            yamlData.remove(ConfigType.DB.key());

            // decoding bean
            Map<String,Object> encryptor = new HashMap<>();
            Map<String,Object> bean = new HashMap<>();
            bean.put("bean", "jasyptStringEncryptor");
            encryptor.put("encryptor", bean);
            yamlData.put("jasypt", encryptor);

            FileWriter writer = new FileWriter(String.valueOf(LOCAL_CONFIG));
            Representer representer = new Representer();
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml newYaml = new Yaml(representer, options);

            newYaml.dump(yamlData, writer);
            writer.close();

            /*---------- if already use the port, kill process before start ----------*/
            // 1. kill by pid file
            if(new File(String.valueOf(LOCAL_PID_FILE)).exists()) {
                Runtime rt = Runtime.getRuntime();
                if (OS.contains("win")) {
                    rt.exec("taskkill /F /PID " + LOCAL_PID_FILE);
                } else {
                    rt.exec("kill -9 " + new BufferedReader(new InputStreamReader(rt.exec("cat " + LOCAL_PID_FILE).getInputStream())).readLine());
                }
                Thread.sleep(1000);
            }

            // 2. kill by port
            int port = (int) yamlData.get(ConfigType.PORT.ecKey());
            if(!isPortAvailable(port)) {
                Runtime rt = Runtime.getRuntime();
                int pid = 0;

                String command = OS.contains("win") ? "netstat -ano" : "lsof -i :" + port;

                ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
                Process process = processBuilder.start();
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.trim().split("\\s+");
                    if (OS.contains("win") && line.contains(":" + port)) {
                        pid = Integer.parseInt(parts[parts.length - 1]);
                    } else if (!OS.contains("win") && line.contains("LISTEN")) {
                        pid = Integer.parseInt(parts[1]);
                    }
                }
                process = OS.contains("win")
                        ? rt.exec("taskkill /F /PID " + pid)
                        : rt.exec("kill -9 " + pid);
                process.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void afterOpen() {
        try {
            Yaml yaml = new Yaml();
            Reader configFile = new FileReader(String.valueOf(LOCAL_CONFIG));
            Map<String, Object> yamlData = yaml.load(configFile);

            /*---------- if openBrowser == true, open browser ----------*/
            if((Boolean) yamlData.get(ConfigType.OPEN_BROWSER.ecKey())) {
                Runtime rt = Runtime.getRuntime();
                String url = "http://127.0.0.1:" + yamlData.get(ConfigType.PORT.ecKey());

                if(OS.contains("win")) {
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if(OS.contains("mac")) {
                    rt.exec("open " + url);
                } else if(OS.contains("nix") || OS.contains("nux")) {
                    rt.exec("xdg-open " + url);
                }
            }

            new File(String.valueOf(LOCAL_CONFIG_DEFAULT)).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFiles(String resource, String local) {
        try {
            InputStream is = EveryCloudApplication.class.getResourceAsStream(resource);
            OutputStream os = new FileOutputStream(local);

            byte[] buffer = new byte[1024];
            int bytes;
            while((bytes = is.read(buffer)) != -1) os.write(buffer, 0, bytes);

            is.close();
            os.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static boolean isPortAvailable(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
