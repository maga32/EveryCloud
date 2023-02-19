package com.everycloud.project.controller.file;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everycloud.project.service.FileService;

@Controller
public class FileViewController {
	
	@Autowired
	FileService fileService;
	
	@RequestMapping("/file")
	public String file(@RequestParam(value="path", required=false, defaultValue="") String path,
			Model model) throws IOException {
		model.addAttribute("path", URLEncoder.encode(path,"utf-8"));
		return "/file/file";
	}
	
	@RequestMapping("/fileList")
	@ResponseBody
	public Map<String,Object> fileList(@RequestParam(value="path", required=false, defaultValue="") String path) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean validPath = fileService.isPathExist(path);

		if(validPath) {
			File nowPath = fileService.getFile(path);
			// windows folder path processing
			path = path.replaceAll("\\\\", "/");
			String realPath = nowPath.getCanonicalPath().replaceAll("\\\\", "/");
			
			if(!realPath.equals(path)) {
				map.put("realPath", URLEncoder.encode(realPath,"utf-8"));
				return map;
			}
			map.put("fileList", fileService.fileList(path));
			map.put("nowPath", nowPath);
		}
		
		map.put("path", path);
		map.put("validPath", validPath);
		
		return map;
	}
}
