package com.project.everycloud.controller;

import com.project.everycloud.model.request.file.FileListLoadDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {

	@PostMapping("/folderList")
	public void testController(@Valid @RequestBody FileListLoadDTO folderListLoad) {
		File file = new File(folderListLoad.getPath());

	}

}
