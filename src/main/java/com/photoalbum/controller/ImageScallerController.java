package com.photoalbum.controller;

import java.io.File;

import org.apache.logging.log4j.core.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.photoalbum.config.AlbumDirectories;
import com.photoalbum.config.ImageScaller;
import com.photoalbum.service.AlbumService;

@Controller
public class ImageScallerController {
	
	@Autowired
	private AlbumService albumService;

	@RequestMapping("/scale/{factor}")
	public String scale(@PathVariable Double factor) {
		
		for (String directoryUrl : AlbumDirectories.getDirectoriesMap().values()) {
			File directory = new File(directoryUrl);
			scale(directory, factor);
		}
		
		return "redirect:/";
		
	}

	private void scale(File file, Double factor) {
		
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				scale(subFile, factor);
			}
		} else if (albumService.getPictureExtensions().contains(FileUtils.getFileExtension(file))) {
			new ImageScaller().scale(file, factor);
		}
	}
	
}
