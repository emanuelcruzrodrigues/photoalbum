package com.photoalbum.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.util.FileUtils;
import org.imgscalr.Scalr;

public class ImageScaller {

	public File scale(File originalFile, int maxWidth, File destinationDir, String newFileName) {
		
		File newFile = new File(destinationDir.getAbsolutePath() + "/" + newFileName);
		
		if (newFile.exists()) return newFile;
		
		try {
			
			LogManager.getLogger(getClass()).info(String.format("Scalling %s with maxWidth %d", originalFile.getAbsolutePath(), maxWidth));

			BufferedImage image = ImageIO.read(originalFile);
			BufferedImage scaledImg = scaleToWidth(image, maxWidth);
			
			String imageFormat = FileUtils.getFileExtension(originalFile);
			
			writeToDisk(scaledImg, newFile, imageFormat);

			LogManager.getLogger(getClass()).info(String.format("%s scalled", newFile.getAbsolutePath()));
			
			return newFile;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private void writeToDisk(BufferedImage scaledImg, File newFile, String imageFormat) throws FileNotFoundException, IOException {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(newFile);
			ImageIO.write(scaledImg, imageFormat, fileOutputStream);
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
	}

	public BufferedImage scaleToWidth(BufferedImage image, int maxWidth) {
		Integer targetHeight = image.getHeight();
		BufferedImage scaledImg = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, maxWidth, targetHeight, Scalr.OP_ANTIALIAS);
		return scaledImg;
	}

	public BufferedImage scaleToHeight(BufferedImage image, int maxHeight) {
		Integer targetWidth = image.getWidth();
		BufferedImage scaledImg = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, targetWidth, maxHeight, Scalr.OP_ANTIALIAS);
		return scaledImg;
	}
	

}
