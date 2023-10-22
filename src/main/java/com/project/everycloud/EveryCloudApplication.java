package com.project.everycloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class EveryCloudApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(EveryCloudApplication.class);
		init(application);
		application.run(args);
	}

	static void init(SpringApplication application) {
		String resource 	= "src/main/resources/datasource/";
//		String resource 	= "BOOT-INF/classes/datasource/";
		String db 			= "EveryCloud.db";
		String config 		= "config.yml";

		Path localFolder 	= Paths.get(System.getProperty("user.home") + File.separator + ".everyCloud");
		Path localDb 		= Paths.get(localFolder + File.separator + db);
		Path localConfig 	= Paths.get(localFolder + File.separator + config);

		if(!Files.exists(localFolder)) new File(localFolder.toString()).mkdir();

		try {
			if(!Files.exists(localDb)) 		Files.copy(new File(resource + db).toPath(), 		localDb);
			if(!Files.exists(localConfig)) 	Files.copy(new File(resource + config).toPath(), 	localConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

