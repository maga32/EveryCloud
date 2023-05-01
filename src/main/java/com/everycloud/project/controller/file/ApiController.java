package com.everycloud.project.controller.file;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.everycloud.project.domain.Share;
import com.everycloud.project.service.file.ShareService;
import com.everycloud.project.util.FileUtil;
import com.everycloud.project.util.UserUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ApiController {
	
	public static final String MAX_SIZE = "128";

	@Autowired
	UserUtil userUtil;

	@Autowired
	FileUtil fileUtil;

	@Autowired
	ShareService shareService;
	
	@RequestMapping("/api/thumbnailMaker")
	public void thumbnailMaker(String name, HttpServletResponse response,
			@RequestParam(value="size", required = false, defaultValue=MAX_SIZE) Integer size,
			@RequestParam(value="shareLink", required=false, defaultValue="") String shareLink) throws IOException {
		if(!userUtil.isAdmin()) {
			if (shareLink.equals("")) return;
			if (!fileUtil.isValidAuth(shareLink)) return;
		}

		Share share = shareService.getShareByLink(shareLink);
		if(share != null) name = share.getPath() + name;

		File file = new File(name);
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

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Image scaledImage = sourceImage.getScaledInstance(width,height, Image.SCALE_SMOOTH);
		
		img.createGraphics().drawImage(scaledImage, 0, 0, null);
		BufferedImage img2 = new BufferedImage(width, height ,BufferedImage.TYPE_INT_RGB);
		img2 = img.getSubimage(0, 0, width, height);
		
		OutputStream os = response.getOutputStream();
		ImageIO.write(img2, FilenameUtils.getExtension(name), os);
	}
	
}
