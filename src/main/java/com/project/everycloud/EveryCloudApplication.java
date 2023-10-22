package com.project.everycloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.project.everycloud.common.config.StarterConfig.afterOpen;
import static com.project.everycloud.common.config.StarterConfig.init;

@SpringBootApplication
public class EveryCloudApplication {

	public static void main(String[] args) {
		init();
		SpringApplication.run(EveryCloudApplication.class, args);
		afterOpen();
	}


}

