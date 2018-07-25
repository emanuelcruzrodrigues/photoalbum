package com.photoalbum.config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import com.photoalbum.tools.FileNormalizer;

public class AlbumDirectories {
	
	private static Map<String, String> directoriesMap = new LinkedHashMap<>();
	
	public static void addAll(String directoriesAsString, boolean normalize) {
		
		directoriesAsString = directoriesAsString.replace("\\", "/");
		
		String[] directoriesArray = directoriesAsString.split(";");
		
		LogManager.getLogger(AlbumDirectories.class).info("--------Configuring directories");
		
		for (String directoryAsString : directoriesArray) {
			try {
				String[] strings = directoryAsString.split("@");
				String label = strings[0];
				String url = strings[1];
				File directory = new File(url);
				
				if(directory.exists()) {
					
					if (normalize) {
						directoriesAsString = new FileNormalizer().normalize(directory);
					}
					
					directoriesMap.put(label, url);
					LogManager.getLogger(AlbumDirectories.class).info(String.format("Directory named '%s' located at '%s' successfully added", label, url));
				}else {
					LogManager.getLogger(AlbumDirectories.class).error(String.format("Directory '%s' could not be added", directoryAsString));
				}
			} catch (Throwable e) {
				LogManager.getLogger(AlbumDirectories.class).error(String.format("Error loading directory '%s'", directoryAsString));
			}
		}
		
		LogManager.getLogger(AlbumDirectories.class).info("--------Directories configuration finished");
		
	}
	
	public static Map<String, String> getDirectoriesMap() {
		return directoriesMap;
	}
	
	public static String getLabel(String directory) {
		for (String label : directoriesMap.keySet()) {
			if (directory.startsWith(directoriesMap.get(label))) return label;
		}
		return null;
	}

	public static String getDirectory(String label) {
		return directoriesMap.get(label);
	}

}
