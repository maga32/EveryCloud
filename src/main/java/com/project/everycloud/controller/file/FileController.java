package com.project.everycloud.controller.file;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.request.file.UpdateFileListDTO;
import com.project.everycloud.model.response.file.FileDetailDTO;
import com.project.everycloud.model.response.file.FileOptionDTO;
import com.project.everycloud.service.FileService;
import com.project.everycloud.service.ShareService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
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

		AppList<FileDetailDTO, FileOptionDTO> fileList = fileService.getFileList(fileListLoad, sessionUser());

		return new AppResponse<AppList<FileDetailDTO, FileOptionDTO>>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(fileList);
	}


	@PostMapping("/folderList")
	public AppResponse<AppList<FileDetailDTO, FileOptionDTO>> getFolderList(@Valid @RequestBody FileListLoadDTO folderListLoad) {

		AppList<FileDetailDTO, FileOptionDTO> folderList = fileService.getFolderList(folderListLoad, sessionUser());

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

		shareService.verifyAuth(shareLink, 0, sessionUser());

		if(StringUtils.hasText(shareLink)) {
			name = shareService.getShareByLink(shareLink).getPath() + name;
		}

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
		Image scaledImage = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		img.createGraphics().drawImage(scaledImage, 0, 0, null);

		OutputStream os = response.getOutputStream();
		ImageIO.write(img, FilenameUtils.getExtension(name), os);
		os.close();
	}


	@GetMapping("/getMetaImage")
	void getMetaImage(HttpServletResponse response) throws IOException {

		String path = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "metaImage.png";
		File file = new File(path);
		BufferedImage sourceImage= ImageIO.read(file);

		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Image scaledImage = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		img.createGraphics().drawImage(scaledImage, 0, 0, null);

		OutputStream os = response.getOutputStream();
		ImageIO.write(img, FilenameUtils.getExtension(file.getPath()), os);
		os.close();
	}

	@GetMapping("/fileDownload")
	void fileDownload(HttpServletResponse response, @RequestParam("path") String path,
					  @RequestParam(value="shareLink", required=false, defaultValue="") String shareLink,
					  @RequestParam("fileNames") String fileNames) throws Exception {

		try {
			shareService.verifyAuth(shareLink, 0, sessionUser());
		} catch(Exception e) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("	alert('" + "권한이 없습니다." + "');");
			out.println("	window.close();");
			out.println("</script>");
			out.close();
			return;
		}

		if(StringUtils.hasText(shareLink)) {
			path = shareService.getShareByLink(shareLink).getPath() + (path.equals("/") ? "" : path);
		}

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
	public AppResponse<Void> newFolder(@Valid @RequestBody NewFileDTO newFolder) {

		fileService.newFile(newFolder, sessionUser(), "folder");

		return new AppResponse<Void>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	@PostMapping("/newFile")
	public AppResponse<Void> newFile(@Valid @RequestBody NewFileDTO newFile) {

		fileService.newFile(newFile, sessionUser(), "file");

		return new AppResponse<Void>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	@PostMapping("/changeName")
	public AppResponse<Void> changeName(@Valid @RequestBody NewFileDTO newFile) {

		fileService.changeName(newFile, sessionUser());

		return new AppResponse<Void>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	@PostMapping("/copyFiles")
	public AppResponse<Void> copyFiles(@Valid @RequestBody UpdateFileListDTO updateFileList) {

		fileService.moveFiles(updateFileList, sessionUser(), "copy");

		return new AppResponse<Void>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	@PostMapping("/moveFiles")
	public AppResponse<Void> moveFiles(@Valid @RequestBody UpdateFileListDTO updateFileList) {

		fileService.moveFiles(updateFileList, sessionUser(), "move");

		return new AppResponse<Void>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	@PostMapping("/deleteFiles")
	public AppResponse<Void> deleteFiles(@Valid @RequestBody UpdateFileListDTO updateFileList) {

		fileService.deleteFiles(updateFileList, sessionUser());

		return new AppResponse<Void>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}


	@GetMapping("/getFavicon")
	public void getFavicon(HttpServletResponse response) throws IOException {
		File file = new File(System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "favicon.ico");
		byte[] fileByte = FileUtils.readFileToByteArray(file);

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" +new String(("favicon.ico").getBytes("utf-8"),"8859_1") +"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setContentLength(fileByte.length);
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}


	private UserDTO sessionUser() {
		return (UserDTO) session.getAttribute("user");
	}

}
