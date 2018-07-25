package com.photoalbum.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

	@Value("${normalize.files}")
	private boolean normalize;
	
	@Value("${album.directories}")
	private String albumDirectoriesAsString;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		AlbumDirectories.addAll(albumDirectoriesAsString, normalize);
		
		Map<String, String> directoriesMap = AlbumDirectories.getDirectoriesMap();
		for (String label : directoriesMap.keySet()) {
			String url = "file:"; 
					
			String path = directoriesMap.get(label);
			
			path.replace("\\", "/");
			
			if (path.startsWith("/")) {
				url += "//" + path;
			}else {
				url += path.replace(":/", "://");
			}
			
			if (!url.endsWith("/")) {
				url = url + "/";
			}
			registry.addResourceHandler(String.format("/%s/**", label)).addResourceLocations(url);
		}
		
//		registry.addResourceHandler("/pictures/**").addResourceLocations("file:///home/emanuel/Pictures/");
		
	}

}
