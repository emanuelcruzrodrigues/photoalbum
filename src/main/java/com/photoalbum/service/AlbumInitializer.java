package com.photoalbum.service;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.photoalbum.config.AlbumDirectories;
import com.photoalbum.domain.Album;
import com.photoalbum.domain.AlbumDirectory;
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
		
		album.clear();
		
		String[] directoriesArray = albumDirectoriesAsString.split(";");
		
		LogManager.getLogger(getClass()).info("--------Configuring album");
		
		for (String directoryAsString : directoriesArray) {
			try {
				String[] strings = directoryAsString.split("@");
				String label = strings[0];
				String url = strings[1];
				File directory = new File(url);
				
				if(directory.exists()) {
					
					LogManager.getLogger(getClass()).info(String.format("Add directory named '%s' located at '%s'", label, directoryAsString));
					
					addToAlbum(label, new File(url));
					
				}else {
					LogManager.getLogger(getClass()).error(String.format("Directory '%s' could not be added", directoryAsString));
				}
			} catch (Throwable e) {
				LogManager.getLogger(getClass()).error(String.format("Error loading directory '%s'", directoryAsString));
			}
		}
		
		LogManager.getLogger(getClass()).info("--------Album configuration finished");
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
			
			for (File subFile : file.listFiles()) {
				addToAlbum(null, subFile);
			}
			
		}else {
			if (!albumExtensions.isValidExtension(file)) return;
			
			AlbumDirectory directory = album.getAlbumDirectory(file.getParentFile());
			if (directory == null) return;
			
			AlbumPicture picture = new AlbumPicture(directory, label, file);
			
			directory.add(picture);
			
			album.add(picture);
			
			ImageScaller imageScaller = new ImageScaller();
			imageScaller.scale(file, 300, AlbumDirectories.getThumbsDirectory(), picture.getId() + "." + picture.getExtension());
			imageScaller.scale(file, 1920, AlbumDirectories.getMidImagesDirectory(), picture.getId() + "." + picture.getExtension());
			
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	private void setRunning(boolean running) {
		this.running = running;
	}
	
	

}
