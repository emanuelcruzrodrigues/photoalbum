package com.photoalbum.domain;

import java.io.File;

import org.apache.logging.log4j.core.util.FileUtils;

public class AlbumPicture extends FileWrapper{
	
	private final AlbumDirectory directory;
	private AlbumPicture priorPicture;
	private AlbumPicture nextPicture;
	private String extension;

	public AlbumPicture(AlbumDirectory directory, String label, File file) {
		super(label, file);
		this.directory = directory;
		this.extension = FileUtils.getFileExtension(file);
	}

	public AlbumPicture getPriorPicture() {
		return priorPicture;
	}

	public void setPriorPicture(AlbumPicture priorPicture) {
		this.priorPicture = priorPicture;
	}

	public AlbumPicture getNextPicture() {
		return nextPicture;
	}

	public void setNextPicture(AlbumPicture nextPicture) {
		this.nextPicture = nextPicture;
	}

	public AlbumDirectory getDirectory() {
		return directory;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public String getThumb() {
		return String.format("../%s/%d.%s", AlbumConstants.LABEL_TEMPORARY_THUMBS, getId(), getExtension());
	}
	
	public String getLink() {
		return String.format("/picture/%d", getId());
	}
	
	public String getMidImage() {
		return String.format("../%s/%d.%s", AlbumConstants.LABEL_TEMPORARY_MID_QUALITY, getId(), getExtension());
	}
	
	public String getPriorPictureLink() {
		if (getPriorPicture() == null) return null;
		return getPriorPicture().getLink();
	}
	
	public String getNextPictureLink() {
		if (getNextPicture() == null) return null;
		return getNextPicture().getLink();
	}
	
	public String getDirectoryLink() {
		return getDirectory().getLink();
	}
	
	public String getDownloadLink() {
		return String.format("/download/%d", getId());
	}

}
