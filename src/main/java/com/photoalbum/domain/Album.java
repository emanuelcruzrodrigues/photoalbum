package com.photoalbum.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;

public class Album {
	
	private static Album instance;
	private Map<Integer, AlbumDirectory> directoriesMap;
	private Map<Integer, AlbumPicture> picturesMap;
	private AlbumPicture priorPicture;
	
	public synchronized static Album getInstance() {
		if (instance == null) {
			instance = new Album();
		}
		return instance;
	}
	
	private Album() {
		super();
	}
	
	public void clear() {
		directoriesMap = new HashMap<>();
		picturesMap = new HashMap<>();
		priorPicture = null;
	}
	
	public Map<Integer, AlbumDirectory> getDirectoriesMap() {
		return directoriesMap;
	}
	
	public AlbumDirectory getAlbumDirectory(File file) {
		return directoriesMap.get(file.getAbsolutePath().hashCode());
	}
	
	public void add(AlbumDirectory directory) {
		directoriesMap.put(directory.getId(), directory);
		priorPicture = null;
		LogManager.getLogger(getClass()).info(String.format("Directory named '%s' located at '%s' added", directory.getName(), directory.getRealPath()));
	}
	
	public void add(AlbumPicture picture) {
		
		picturesMap.put(picture.getId(), picture);
		
		if (priorPicture != null) {
			picture.setPriorPicture(priorPicture);
			priorPicture.setNextPicture(picture);
		}
		priorPicture = picture;
		
		LogManager.getLogger(getClass()).info(String.format("Picture named '%s' located at '%s' added", picture.getName(), picture.getRealPath()));
	}

	public AlbumDirectory getAlbumDirectory(Integer id) {
		return directoriesMap.get(id);
	}

	public AlbumPicture getAlbumPicture(Integer id) {
		return picturesMap.get(id);
	}

	public AlbumPicture getRandomPicture() {
		if (picturesMap.size() == 0) return null;
		
		int idx = new Random().nextInt(picturesMap.size());
		return new ArrayList<>(picturesMap.values()).get(idx);
	}
	

}
