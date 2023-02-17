package com.everycloud.project.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.everycloud.project.service.FileService;

@Controller
public class FileViewController {
	
	@Autowired
	FileService fileService;
	
	@RequestMapping("/file")
	public String files(@RequestParam(value="path", required=false, defaultValue="") String path,
			Model model) throws IOException {

		boolean validPath = fileService.isPathExist(path);

		if(validPath) {
			File nowPath = fileService.getFile(path);
			// windows folder path processing
			path = path.replaceAll("\\\\", "/");
			String realpath = nowPath.getCanonicalPath().replaceAll("\\\\", "/");
			
			if(!realpath.equals(path)) {
				return "redirect:/file?path=" + realpath;
			}
			model.addAttribute("fileList", fileService.getPathFiles(path));
			model.addAttribute("nowPath", nowPath);
		}
		
		model.addAttribute("path", path);
		model.addAttribute("validPath", validPath);
		
		return "/file";
	}
}
