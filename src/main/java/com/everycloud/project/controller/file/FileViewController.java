package com.everycloud.project.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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
	public String file(@RequestParam(value="path", required=false, defaultValue="/") String path,
			@RequestParam(value="sort", required=false, defaultValue="name") String sort,
			@RequestParam(value="order", required=false, defaultValue="asc") String order,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
			Model model) throws IOException {
		model.addAttribute("path", URLEncoder.encode(path,"utf-8"));
		model.addAttribute("sort", sort);
		model.addAttribute("order",order);
		model.addAttribute("keyword",keyword);
		
		return "/file/file";
	}
	
	@RequestMapping("/fileList")
	@ResponseBody
	public Map<String,Object> fileList(@RequestParam(value="path", required=false, defaultValue="") String path,
			@RequestParam(value="sort", required=false, defaultValue="name") String sort,
			@RequestParam(value="order", required=false, defaultValue="asc") String order,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
			@RequestParam(value="viewHidden", required=false) boolean viewHidden) throws IOException {
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
			map.put("fileList", fileService.fileList(path, sort, order, keyword, viewHidden));
			map.put("nowPath", nowPath);
		}
		
		map.put("path", path);
		map.put("validPath", validPath);
		
		return map;
	}
	
	@RequestMapping("/chageName")
	@ResponseBody
	public Map<String,Object> chageName(@RequestParam("path") String path,
			@RequestParam("origFileName") String origFileName, @RequestParam("newFileName") String newFileName) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		fileService.changeName(path, origFileName, newFileName);
		
		map.put("result", "ok");
		return map;
	}
	
	@RequestMapping("/fileDownload")
	void fileDownload(HttpServletResponse response, @RequestParam("path") String path,
			@RequestParam("fileNames") String fileNames) throws Exception {
		String[] fileList = fileNames.split(",");
		File firstFile = new File(path+ File.separator + fileList[0]);
		
		if(fileList.length!=1 || firstFile.isDirectory()) { // when multiple files selected or selected one is folder.
			String downName ="downloads";
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(downName.getBytes("utf-8"),"8859_1")+".zip" + "\";");
			ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
			
        	for (String fileName : fileList) {
        		File file = new File(path + File.separator + fileName);
        		addDownloadFile(out, file, "");
            }
        	
        	out.close();
        	
		} else {	// when selected only one file
			if(firstFile.isFile()) {
				byte[] fileByte = FileUtils.readFileToByteArray(firstFile);
				
    			response.setContentType("application/octet-stream");
    			response.setHeader("Content-Disposition", "attachment; fileName=\"" +new String(fileList[0].getBytes("utf-8"),"8859_1") +"\";");
    			response.setHeader("Content-Transfer-Encoding", "binary");
    			response.setContentLength(fileByte.length);
    			response.getOutputStream().write(fileByte);
    			response.getOutputStream().flush();
    			response.getOutputStream().close();
            }
		}
	}
	
	private static void addDownloadFile(ZipOutputStream out, File file, String path) throws IOException {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				addDownloadFile(out, f, path + file.getName() + File.separator);
			}
			return;
		}

		FileInputStream in = new FileInputStream(file);
		out.putNextEntry(new ZipEntry(path + file.getName()));

		int len;
		byte[] buf = new byte[4096];
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.closeEntry();
	}


}
