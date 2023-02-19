package com.everycloud.project.controller.file;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiController {
	
	public static final int MAX_SIZE = 64;
	
	@RequestMapping("/api/thumbnailmaker")
	public void thumbnailMaker(String name, HttpServletResponse response) throws IOException {
		
		File file = new File(name);
		BufferedImage sourceImage= ImageIO.read(file);
		
		int width = sourceImage.getWidth();
	    int height = sourceImage.getHeight();
	    
	    if(width >= height && width > MAX_SIZE) {
	        height = (int) (height * (MAX_SIZE / (float) width));
	        width = MAX_SIZE;
	    } else if (height > MAX_SIZE) {
	        width = (int) (width * (MAX_SIZE / (float) height));
	        height = MAX_SIZE;
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
