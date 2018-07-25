package com.photoalbum.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.photoalbum.config.AlbumDirectories;

@Service
public class AlbumService {
	
	@Value("${picture.extensions}")
	private String extensionsAsString;
	
	private List<String> extensions;
	
	public Map<String, String> getSubFolders() {
		
		Map<String, String> result = new LinkedHashMap<>();
		
		for (String label : AlbumDirectories.getDirectoriesMap().keySet()) {
			String path = encodePath(AlbumDirectories.getDirectoriesMap().get(label));
			result.put(label, path);
		}
		
		return result;
	}
	
	public Map<String, String> getSubFolders(String path){
		
		path = decodePath(path);
		
		List<String> directories = Arrays.asList(new File(path).listFiles()).stream()
				.filter(f -> f.isDirectory())
				.map(f -> f.getAbsolutePath())
				.sorted()
				.collect(Collectors.toList());
		
		
		Map<String, String> result = new LinkedHashMap<>();
		
		for (String directory : directories) {
			String label = new File(directory).getName();
			result.put(label, encodePath(directory));
		}
		
		
		return result;
	}
	
	public String encodePath(String s) {
		return s.replace("/", "@").replace("\\", "@");
	}
	
	public String decodePath(String path) {
		return path.replace("@", "/");
	}

	public Map<String, String> getPictures(String path) {
		
		path = decodePath(path);
		
		LogManager.getLogger().info("Read images from: " + path);
		
		String label = AlbumDirectories.getLabel(path);
		LogManager.getLogger().info("label: " + label);
		
		String root = AlbumDirectories.getDirectory(label);
		LogManager.getLogger().info("root: " + root);
		
		Map<String, String> result = new LinkedHashMap<>();
		
		List<File> files = new ArrayList<>(Arrays.asList(new File(path).listFiles()));
		Collections.sort(files);
		
		for (File file : files) {
			if (!getPictureExtensions().contains(FileUtils.getFileExtension(file))) continue;
			String picturePath = file.getAbsolutePath().replace("\\", "/").replace(root, "");
			LogManager.getLogger().info("picture: " + picturePath);
			String url = String.format("../%s%s%s", label, (picturePath.startsWith("/") ? "" : "/"), picturePath);
			result.put(file.getName(), url);
		}
		
		return result;
	}
	
	public List<String> getPictureExtensions() {
		if (extensions == null) {
			extensions = Arrays.asList(extensionsAsString.split(";"));
		}
		return extensions;
	}

}
