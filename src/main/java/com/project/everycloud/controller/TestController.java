package com.project.everycloud.controller;

import com.project.everycloud.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {
	@Autowired
	SettingsService settingsService;

	@GetMapping("/setPort")
	public void testController(int port) {
		settingsService.setPort(port);
	}

}
