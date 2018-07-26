package com.photoalbum.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumDirectory extends FileWrapper {
	
	private AlbumDirectory parentDirectory;
	private final List<AlbumDirectory> subDirectories;
	private final List<AlbumPicture> pictures;
	
	public AlbumDirectory(String label, File file) {
		super(label, file);
		this.subDirectories = new ArrayList<>();
		this.pictures = new ArrayList<>();
	}
	
	public List<AlbumDirectory> getDirectoryPath() {
		if (parentDirectory == null) {
			List<AlbumDirectory> result = new ArrayList<>();
			result.add(this);
			return result;
		}
		
		List<AlbumDirectory> result = parentDirectory.getDirectoryPath();
		result.add(this);
		
		return result;
	}
	
	public List<AlbumDirectory> getSubDirectories() {
		return subDirectories;
	}

	public List<AlbumPicture> getPictures() {
		return pictures;
	}

	public AlbumDirectory getParentDirectory() {
		return parentDirectory;
	}
	
	public void setParentDirectory(AlbumDirectory parentDirectory) {
		this.parentDirectory = parentDirectory;
	}
	
	public void addSubDirectory(AlbumDirectory directory) {
		subDirectories.add(directory);
	}

	public void add(AlbumPicture picture) {
		pictures.add(picture);
	}
	
	public String getLink() {
		return String.format("/folder/%d", getId());
	}
	

}
