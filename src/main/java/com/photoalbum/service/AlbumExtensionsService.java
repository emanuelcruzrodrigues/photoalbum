package com.photoalbum.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlbumExtensionsService {
	
	@Value("${picture.extensions}")
	private String extensionsAsString;
	
	private List<String> extensions;
	
	public boolean isValidExtension(File file) {
		String extension = FileUtils.getFileExtension(file);
		if (extension == null) return false;
		
		return getPictureExtensions().contains(extension.toLowerCase());
	}
	
	public List<String> getPictureExtensions() {
		if (extensions == null) {
			extensions = Arrays.asList(extensionsAsString.toLowerCase().split(","));
		}
		return extensions;
	}

}
