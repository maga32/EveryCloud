package com.project.everycloud.controller.file;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.response.file.FileDetailDTO;
import com.project.everycloud.model.response.file.FileOptionDTO;
import com.project.everycloud.service.FileService;
import com.project.everycloud.service.ShareService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/v1")
public class FileController {
	
	@Autowired
	FileService fileService;

	@Autowired
	ShareService shareService;

	@Autowired
	HttpSession session;

	@PostMapping("/fileList")
	public AppResponse<AppList<FileDetailDTO, FileOptionDTO>> getFileList(@Valid @RequestBody FileListLoadDTO fileListLoad) {

		UserDTO sessionUser = (UserDTO) session.getAttribute("user");
		AppList<FileDetailDTO, FileOptionDTO> fileList = fileService.fileList(fileListLoad, sessionUser);

		return new AppResponse<AppList<FileDetailDTO, FileOptionDTO>>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(fileList);
	}


	/* --------------------------- 수정필요 --------------------------- */

	@RequestMapping("/fileList2")
	public AppResponse<HashMap<String,Object>> fileList(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam(value="path", required=false, defaultValue="") String path,
			@RequestParam(value="sort", required=false, defaultValue="name") String sort,
			@RequestParam(value="order", required=false, defaultValue="asc") String order,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
			@RequestParam(value="viewHidden", required=false) boolean viewHidden) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
//		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 0);
//		map.putAll(shareMap);

		String sharePath = "";

		if(path.equals("") && shareLink.equals("")) path += "/";
//		if(shareMap.get("invalidString") != null) {
//			return map;
//		} else if(!shareLink.equals("")) {
//			sharePath = shareMap.get("sharePath");
//			path = sharePath + (path.equals("/") ? "" : path);
//		}
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		// boolean validPath = fileService.isPathExist(path);

		// if(validPath) {
		// File nowPath = fileService.getFile(path);
		// windows folder path processing
		path = path.replaceAll("\\\\", "/");
		// String realPath = nowPath.getCanonicalPath().replaceAll("\\\\", "/");

		// if(!realPath.equals(path)) {
		// return fileList(shareLink, realPath.replace(sharePath, ""), sort, order, keyword, viewHidden);
		// }

		// map.put("nowPath", nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
		// map.put("fileList", fileService.fileList(sharePath, path, sort, order, keyword, viewHidden));
		// }

		map.put("path", path.replace(sharePath, ""));
		// map.put("validPath", validPath);

		return new AppResponse<HashMap<String, Object>>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData((HashMap<String, Object>) map);
	}

	@RequestMapping("/folderList")
	public Map<String,Object> folderList(@RequestParam("path") String path,
			@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 0, session);
		map.putAll(shareMap);

		String sharePath = "";

		if(shareMap.get("invalidString") != null) {
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		// boolean validPath = fileService.isPathExist(path);

		// if(validPath) {
			// File nowPath = fileService.getFile(path);
			// windows folder path processing
			path = path.replaceAll("\\\\", "/");
			// String parentPath = nowPath.getPath().replaceAll("\\\\", "/").length() > sharePath.length() ? nowPath.getParent().replaceAll("\\\\", "/").replace(sharePath, "") : "/";
			map.put("folderList", fileService.folderList(sharePath, path));
			// map.put("nowPath", nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
			// map.put("parentPath", parentPath);
		// }
		
		map.put("path", path.replace(sharePath, ""));
		// map.put("validPath", validPath);
		
		return map;
	}
	
	@RequestMapping("/newFolder")
	public Map<String,Object> newFolder(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam("path") String path, @RequestParam("newFolderName") String newFolderName) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}

		map.putAll(fileService.newFolder(path, newFolderName));
		
		return map;
	}
	
	@RequestMapping("/newFile")
	public Map<String,Object> newFile(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam("path") String path, @RequestParam("newFileName") String newFileName) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}

		map.putAll(fileService.newFile(path, newFileName));
		
		return map;
	}
	
	@RequestMapping("/chageName")
	public Map<String,Object> chageName(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam("path") String path, @RequestParam("origFileName") String origFileName,
			@RequestParam("newFileName") String newFileName) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}

		fileService.changeName(path, origFileName, newFileName);
		
		map.put("result", "ok");
		return map;
	}
	
	@RequestMapping("/fileDownload")
	void fileDownload(HttpServletResponse response, @RequestParam("path") String path,
			@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam("fileNames") String fileNames) throws Exception {
		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 0, session);
		String sharePath = "";

		String invalidString = "" + (shareMap.get("invalidString") != null ? shareMap.get("invalidString") : "");

		if(!invalidString.equals("")) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("	alert('" + invalidString + "');");
			out.println("	window.close();");
			out.println("</script>");
			out.close();
			return;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}

		String[] fileList = fileNames.split(",");
		File firstFile = new File(path + File.separator + fileList[0]);
		
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
	public Map<String,Object> moveFiles(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam("fileNames") String fileNames, @RequestParam("type") String type,
			@RequestParam("path") String path,  @RequestParam("moveToPath") String moveToPath) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
			moveToPath = sharePath + (moveToPath.equals("/") ? "" : moveToPath);
		}

		map.putAll(fileService.moveFiles(path, moveToPath, fileNames, type));
		
		return map;
	}
	
	@RequestMapping("/deleteFiles")
	public Map<String,Object> deleteFiles(@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
			@RequestParam("path") String path, @RequestParam("fileNames") String fileNames) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}

		map.putAll(fileService.deleteFiles(path,fileNames));
		
		return map;
	}

}
