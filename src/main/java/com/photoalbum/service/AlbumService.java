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
import com.photoalbum.tools.ImageScaller;

@Service
public class AlbumService {
	
	@Value("${picture.extensions}")
	private String extensionsAsString;
	
	private List<String> extensions;
	
	public Map<String, String> getSubFolders() {
		
		Map<String, String> result = new LinkedHashMap<>();
		
		for (String label : AlbumDirectories.getDirectoriesMap().keySet()) {
			result.put(label, label);
		}
		
		return result;
	}
	
	public Map<String, String> getSubFolders(String path){
		
		String realPath = getRealPath(path);
		
		List<String> directories = Arrays.asList(new File(realPath).listFiles()).stream()
				.filter(f -> f.isDirectory())
				.map(f -> f.getAbsolutePath())
				.sorted()
				.collect(Collectors.toList());
		
		
		Map<String, String> result = new LinkedHashMap<>();
		
		String label = AlbumDirectories.getLabel(realPath);
		
		String root = AlbumDirectories.getDirectory(label);
		
		for (String directory : directories) {
			String subDirectoryLabel = new File(directory).getName();
			String subDirectory = encodePath(directory.replace("\\", "/").replace(root, label));
			result.put(subDirectoryLabel, subDirectory);
		}
		
		
		return result;
	}

	public String[] getPriorAndNextPicturesPaths(String path, String pictureName) {
		path = getRealPath(path);
		
		String label = AlbumDirectories.getLabel(path);
		String root = AlbumDirectories.getDirectory(label);
		
		String[] result = new String[2];
		
		List<File> pictures = getPictures(path, false);
		
		for (int i = 0; i < pictures.size(); i++) {
			File picture = pictures.get(i);
			
			if (picture.getName().equals(pictureName)) {
				if (pictures.size() > i+1) {
					result[1] = encodePath(label, root, pictures.get(i+1), "_mid");
				}
				
				return result;
			}else {
				result[0] = encodePath(label, root, picture, "_mid");
			}
		}
		
		result[0] = null;
		result[1] = null;
		return result;
	}

	
	public Map<String, String> getThumbsPaths(String path) {
		return getPicturesPaths(path, true);
	}
	
	public Map<String, String> getPicturesPaths(String path, boolean thumbs) {
		path = getRealPath(path);
		
		String label = AlbumDirectories.getLabel(path);
		String root = AlbumDirectories.getDirectory(label);
		
		Map<String, String> result = new LinkedHashMap<>();
		
		List<File> files = getPictures(path, thumbs);
		
		for (File file : files) {
			String encodedPath = encodePath(label, root, file);
			result.put(file.getName(), encodedPath);
		}
		
		return result;
	}

	public List<File> getPictures(String realPath, boolean thumbs) {
		
		List<File> result = new ArrayList<>();
		
		LogManager.getLogger().info("Read images from: " + realPath);
		
		List<File> files = new ArrayList<>(Arrays.asList(new File(realPath).listFiles()));
		Collections.sort(files);
		
		for (File file : files) {
			if (!file.isFile()) continue;
			if (file.getName().startsWith("_thumb")) continue;
			if (file.getName().startsWith("_mid")) continue;
			if (!getPictureExtensions().contains(FileUtils.getFileExtension(file).toLowerCase())) continue;
			
			if (thumbs) {
				File thumb = new ImageScaller().scale(file, 400, "_thumb");
				new ImageScaller().scale(file, 1920, "_mid");
				if (thumb == null) continue;
				
				
				LogManager.getLogger().info("thumb: " + thumb.getName());
				result.add(thumb);
			
			}else {
				result.add(file);
				LogManager.getLogger().info("picture: " + file.getName());
			}
		}
		
		return result;
	}
	
	private String encodePath(String label, String root, File file) {
		return encodePath(label, root, file, null);
	}
	
	private String encodePath(String label, String root, File file, String fileNamePreffix) {
		
		String fileName = file.getName();
		if (fileNamePreffix != null) {
			fileName = fileNamePreffix + fileName;
		}
		
		String filePath = file.getAbsolutePath().replace("\\", "/").replace(root, "").replace(file.getName(), fileName);
		String url = String.format("../%s%s%s", label, (filePath.startsWith("/") ? "" : "/"), filePath);
		String encodedUrl = encodePath(url);
		return encodedUrl;
	}
	
	public String encodePath(String s) {
		return s.replace("/", "@").replace("\\", "@");
	}
	
	public String decodePath(String path) {
		return path.replace("@", "/");
	}
	
	public String getRealPath(String path) {
		String rootLabel = path.split("/")[0];
		String root = AlbumDirectories.getDirectory(rootLabel);
		String realPath = path.replace(rootLabel, root);
		return realPath;
	}
	
	public List<String> getPictureExtensions() {
		if (extensions == null) {
			extensions = Arrays.asList(extensionsAsString.toLowerCase().split(";"));
		}
		return extensions;
	}


}
