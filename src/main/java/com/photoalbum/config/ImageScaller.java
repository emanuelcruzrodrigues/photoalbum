package com.photoalbum.config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.util.FileUtils;
import org.imgscalr.Scalr;

public class ImageScaller {
	
	public void scale(File file, Double factor) {
		try {
			LogManager.getLogger(getClass()).info(String.format("Scalling %s with factor %f", file.getAbsolutePath(), factor));
			
	        String imageFormat = FileUtils.getFileExtension(file);
	        BufferedImage image = ImageIO.read(file);
	        Double targetWidth = image.getWidth() * factor;
	        Integer targetHeight = image.getHeight();
	 
	 
	        BufferedImage scaledImg = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetWidth.intValue(), targetHeight, Scalr.OP_ANTIALIAS);
	 
	        FileOutputStream fileOutputStream = new FileOutputStream(file);
			ImageIO.write(scaledImg, imageFormat, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
	        
	        LogManager.getLogger(getClass()).info(String.format("%s scalled", file.getAbsolutePath()));
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
