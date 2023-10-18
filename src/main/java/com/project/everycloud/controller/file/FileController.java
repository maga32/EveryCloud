package com.project.everycloud.controller.file;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.response.file.FileDetailDTO;
import com.project.everycloud.model.response.file.FileOptionDTO;
import com.project.everycloud.service.FileService;
import com.project.everycloud.service.ShareService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/v1/file")
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
		AppList<FileDetailDTO, FileOptionDTO> fileList = fileService.getFileList(fileListLoad, sessionUser);

		return new AppResponse<AppList<FileDetailDTO, FileOptionDTO>>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(fileList);
	}


	@PostMapping("/folderList")
	public AppResponse<AppList<FileDetailDTO, FileOptionDTO>> getFolderList(@Valid @RequestBody FileListLoadDTO folderListLoad) {

		UserDTO sessionUser = (UserDTO) session.getAttribute("user");
		AppList<FileDetailDTO, FileOptionDTO> folderList = fileService.getFolderList(folderListLoad, sessionUser);

		return new AppResponse<AppList<FileDetailDTO, FileOptionDTO>>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(folderList);
	}


	public static final String DEFAULT_SIZE = "128";

	@GetMapping("/thumbnailMaker")
	public void thumbnailMaker(HttpServletResponse response,
			String name,
			@RequestParam(value="size", required = false, defaultValue=DEFAULT_SIZE) Integer size,
			@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink) throws IOException {
		/*
		if(!userUtil.isAdmin()) {
			if (shareLink.equals("")) return;
			if (fileUtil.hasValidAuth(shareLink,0) != 1) return;
		}

		Share share = shareService.getShareByLink(shareLink);
		if(share != null) name = share.getPath() + name;
		*/

		File file = new File(name);
		String extension = FilenameUtils.getExtension(name).toLowerCase();
		boolean transparent = extension.equals("png") || extension.equals("gif");
		BufferedImage sourceImage= ImageIO.read(file);

		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();

		// size 0 is original size
		if(size != 0) {
			if (width >= height && width > size) {
				height = (int) (height * (size / (float) width));
				width = size;
			} else if (height > size) {
				width = (int) (width * (size / (float) height));
				height = size;
			}
		}

		BufferedImage img = new BufferedImage(width, height, (transparent ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB));
		Image scaledImage = sourceImage.getScaledInstance(width,height, Image.SCALE_SMOOTH);

		img.createGraphics().drawImage(scaledImage, 0, 0, null);

		OutputStream os = response.getOutputStream();
		ImageIO.write(img, FilenameUtils.getExtension(name), os);
		os.close();
	}


	@GetMapping("/fileDownload")
	void fileDownload(HttpServletResponse response, @RequestParam("path") String path,
					  @RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
					  @RequestParam("fileNames") String fileNames) throws Exception {
		// Map<String, String> shareMap = shareService.getShareAuth(shareLink, 0, session);
		// String sharePath = "";

		// String invalidString = "" + (shareMap.get("invalidString") != null ? shareMap.get("invalidString") : "");
		/*
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
		*/
		String[] fileList = fileNames.split(":/:");
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


	@PostMapping("/newFolder")
	public AppResponse<Integer> newFolder(@Valid @RequestBody NewFileDTO newFolder) {

		UserDTO sessionUser = (UserDTO) session.getAttribute("user");
		fileService.newFile(newFolder, sessionUser, "folder");

		return new AppResponse<Integer>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	@PostMapping("/newFile")
	public AppResponse<Integer> newFile(@Valid @RequestBody NewFileDTO newFile) {

		UserDTO sessionUser = (UserDTO) session.getAttribute("user");
		fileService.newFile(newFile, sessionUser, "file");

		return new AppResponse<Integer>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	/* --------------------------- 수정필요 --------------------------- */
	
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
