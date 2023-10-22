package com.project.everycloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {

	@GetMapping("")
	public void testController(int port) {
		String filePath = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "config.yml";

		try {
			Yaml yaml = new Yaml();
			Reader yamlFile = new FileReader(filePath);
			Map<String, Object> yamlData = yaml.load(yamlFile);
			Map<String, Object> portData = new HashMap<String, Object>();
			portData.put("port", port);
			yamlData.put("server", portData);

			FileWriter writer = new FileWriter(filePath);
			Representer representer = new Representer();
			DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			Yaml newYaml = new Yaml(representer, options);

			newYaml.dump(yamlData, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
