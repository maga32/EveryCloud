package com.everycloud.project.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.everycloud.project.domain.Share;
import com.everycloud.project.service.file.ShareService;
import com.everycloud.project.util.UserUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everycloud.project.service.file.FileService;

@Controller
public class FileViewController {
	
	@Autowired
	FileService fileService;

	@Autowired
	ShareService shareService;

	@Autowired
	UserUtil userUtil;

	@RequestMapping("/file")
	public String file(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam(value="path", required=false, defaultValue="") String path,
			@RequestParam(value="sort", required=false, defaultValue="name") String sort,
			@RequestParam(value="order", required=false, defaultValue="asc") String order,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
			Model model) throws IOException {
		model.addAttribute("shareLink", shareLink);
		model.addAttribute("path", URLEncoder.encode(path,"utf-8"));
		model.addAttribute("sort", sort);
		model.addAttribute("order",order);
		model.addAttribute("keyword",keyword);
		
		return "/file/file";
	}

	@RequestMapping("/fileList")
	@ResponseBody
	public Map<String,Object> fileList(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam(value="path", required=false, defaultValue="") String path,
			@RequestParam(value="sort", required=false, defaultValue="name") String sort,
			@RequestParam(value="order", required=false, defaultValue="asc") String order,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
			@RequestParam(value="viewHidden", required=false) boolean viewHidden) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("shareLink",shareLink);
		String sharePath = "";

		if(path.equals("") && shareLink.equals("")) path += "/";

		if(shareLink.equals("") && !userUtil.isAdmin()) {
			map.put("invalidAuth", "관리자만 접근할 수 있습니다.");
			return map;
		} else if(!shareLink.equals("")) {
			Share share = shareService.getShareByLink(shareLink);
			sharePath = share.getPath();
			path = sharePath + (path.equals("/") ? "" : path);
		}
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		boolean validPath = fileService.isPathExist(path);

		if(validPath) {
			File nowPath = fileService.getFile(path);
			// windows folder path processing
			path = path.replaceAll("\\\\", "/");
			String realPath = nowPath.getCanonicalPath().replaceAll("\\\\", "/");

			if(!realPath.equals(path)) {
				return fileList(shareLink, realPath.replace(sharePath, ""), sort, order, keyword, viewHidden);
			}

			map.put("nowPath", nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
			map.put("fileList", fileService.fileList(sharePath, path, sort, order, keyword, viewHidden));
		}

		map.put("path", path.replace(sharePath, ""));
		map.put("validPath", validPath);
		
		return map;
	}
	
	@RequestMapping("/folderList")
	@ResponseBody
	public Map<String,Object> folderList(@RequestParam("path") String path) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean validPath = fileService.isPathExist(path);

		if(validPath) {
			File nowPath = fileService.getFile(path);
			// windows folder path processing
			path = path.replaceAll("\\\\", "/");
			
			map.put("folderList", fileService.folderList(path));
			map.put("nowPath", nowPath);
		}
		
		map.put("path", path);
		map.put("validPath", validPath);
		
		return map;
	}
	
	@RequestMapping("/newFolder")
	@ResponseBody
	public Map<String,Object> newFolder(@RequestParam("path") String path, @RequestParam("newFolderName") String newFolderName) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.putAll(fileService.newFolder(path, newFolderName));
		
		return map;
	}
	
	@RequestMapping("/newFile")
	@ResponseBody
	public Map<String,Object> newFile(@RequestParam("path") String path, @RequestParam("newFileName") String newFileName) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.putAll(fileService.newFile(path, newFileName));
		
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

	@RequestMapping("/moveFiles")
	@ResponseBody
	public Map<String,Object> moveFiles(@RequestParam("fileNames") String fileNames, @RequestParam("type") String type,
			@RequestParam("path") String path,  @RequestParam("moveToPath") String moveToPath) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.putAll(fileService.moveFiles(path, moveToPath, fileNames, type));
		
		return map;
	}
	
	@RequestMapping("/deleteFiles")
	@ResponseBody
	public Map<String,Object> deleteFiles(@RequestParam("path") String path, @RequestParam("fileNames") String fileNames) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.putAll(fileService.deleteFiles(path,fileNames));
		
		return map;
	}
}
