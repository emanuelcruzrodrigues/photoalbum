package com.photoalbum.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.photoalbum.domain.AlbumConstants;
import com.photoalbum.domain.AlbumDirectory;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		List<AlbumDirectory> directories = new ArrayList<>();
		
		File thumbsDirectory = AlbumDirectories.getThumbsDirectory();
		directories.add(new AlbumDirectory(AlbumConstants.LABEL_TEMPORARY_THUMBS, thumbsDirectory));
		
		File midImagesDirectory = AlbumDirectories.getMidImagesDirectory();
		directories.add(new AlbumDirectory(AlbumConstants.LABEL_TEMPORARY_MID_QUALITY, midImagesDirectory));
		
		for (AlbumDirectory albumDirectory : directories) {
			String label = albumDirectory.getLabel();
			if (label == null) continue;

			String url = "file:";

			String path = albumDirectory.getRealPath();

			path.replace("\\", "/");

			if (path.startsWith("/")) {
				url += "//" + path;
			} else {
				url += path.replace(":/", "://");
			}

			if (!url.endsWith("/")) {
				url = url + "/";
			}
			
			registry.addResourceHandler(String.format("/%s/**", label)).addResourceLocations(url);
		}
		
		//example: registry.addResourceHandler("/pictures/**").addResourceLocations("file:///home/emanuel/Pictures/");
		
	}

}
