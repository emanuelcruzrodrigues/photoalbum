package com.photoalbum.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.photoalbum.config.AlbumDirectories;
import com.photoalbum.domain.Album;
import com.photoalbum.domain.AlbumDirectory;
import com.photoalbum.domain.AlbumFileComparator;
import com.photoalbum.domain.AlbumPicture;
import com.photoalbum.tools.ImageScaller;

@Service
public class AlbumInitializer {
	
	private boolean running;
	
	private final Album album;
	
	@Autowired
	private AlbumExtensionsService albumExtensions;
	
	@Value("${album.directories}")
	private String albumDirectoriesAsString;

	private double progress;
	
	public AlbumInitializer() {
		this.album = Album.getInstance();
		this.running = false;
	}

	public void run() {
		
		if (isRunning()) return;
		
		setRunning(true);
		
		try {
		
			initializeAlbum();
			
		} finally {
			setRunning(false);
		}
		
		
	}

	private synchronized void initializeAlbum() {
		progress = 0;
		addAllPicturesToAlbum();
		generateScalledImages();
		progress = 100;
	}

	private void addAllPicturesToAlbum() {
		album.clear();
		
		Logger logger = LogManager.getLogger(getClass());
		logger.info("--------Configuring album");
		
		String[] directoriesArray = albumDirectoriesAsString.split(";");
		
		for (String directoryAsString : directoriesArray) {
			try {
				String[] strings = directoryAsString.split("@");
				String label = strings[0];
				String url = strings[1];
				File directory = new File(url);
				
				if(directory.exists()) {
					
					logger.info(String.format("Add directory named '%s' located at '%s'", label, directoryAsString));
					
					addToAlbum(label, new File(url));
					
				}else {
					logger.error(String.format("Directory '%s' could not be added", directoryAsString));
				}
			} catch (Throwable e) {
				logger.error(String.format("Error loading directory '%s'", directoryAsString));
				e.printStackTrace();
			}
		}
		
		logger.info("--------Album configuration finished");
	}

	private void addToAlbum(String label, File file) {
		
		if (file.isDirectory()) {
			
			AlbumDirectory directory = new AlbumDirectory(label, file);
			album.add(directory);
			
			AlbumDirectory parentDirectory = album.getAlbumDirectory(file.getParentFile());
			if (parentDirectory != null) {
				directory.setParentDirectory(parentDirectory);
				parentDirectory.addSubDirectory(directory);
			}
			
			File[] subFiles = file.listFiles();
			if (subFiles == null) return;
			
			List<File> subFilesList = new ArrayList<>(Arrays.asList(subFiles));
			Collections.sort(subFilesList, new AlbumFileComparator());
			
			for (File subFile : subFilesList) {
				addToAlbum(null, subFile);
			}
			
			album.setPriorPicture(null);
			
		}else {
			if (!albumExtensions.isValidExtension(file)) return;
			
			AlbumDirectory directory = album.getAlbumDirectory(file.getParentFile());
			if (directory == null) return;
			
			AlbumPicture picture = new AlbumPicture(directory, label, file);
			
			directory.add(picture);
			
			album.add(picture);
			
		}
	}
	
	private void generateScalledImages() {
		Logger logger = LogManager.getLogger(getClass());
		
		ImageScaller imageScaller = new ImageScaller();
		
		List<AlbumPicture> pictures = new ArrayList<>(album.getPicturesMap().values());
		
		logger.info(String.format("--------Gerenate %d scalled images", pictures.size()));
		
		for (int i = 0; i < pictures.size(); i++) {
			
			AlbumPicture picture = pictures.get(i);
			
			File file = new File(picture.getRealPath());
			
			imageScaller.scale(file, 400, AlbumDirectories.getThumbsDirectory(), picture.getId() + "." + picture.getExtension());
			imageScaller.scale(file, 1920, AlbumDirectories.getMidImagesDirectory(), picture.getId() + "." + picture.getExtension());
			
			if (i % 10 == 0) {
				progress = (new Integer(i).doubleValue() / pictures.size()) * 100d; 
				logger.info(String.format("Picture %d / %d (%.02f%%)", i, pictures.size(), progress));
			}
		}
		
		logger.info("--------All images scalled");
	}
	
	public boolean isRunning() {
		return running;
	}

	private void setRunning(boolean running) {
		this.running = running;
	}
	
	public double getProgress() {
		return progress;
	}

}
