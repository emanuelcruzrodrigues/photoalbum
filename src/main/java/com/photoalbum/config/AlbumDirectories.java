package com.photoalbum.config;

import java.io.File;

import com.photoalbum.domain.AlbumConstants;

public class AlbumDirectories {
	
	private static File tempDir;
	private static File thumbsDir;
	private static File midImagesDir;
	
	public static File getTemporaryDirectory() {
		if (tempDir == null) {
			tempDir = new File(AlbumConstants.PHOTO_ALBUM_TEMP_DIR);
			if (!tempDir.exists()) {
				tempDir.mkdirs();
			}
		}
		return tempDir;
	}
	
	public static File getThumbsDirectory() {
		if (thumbsDir == null) {
			thumbsDir = new File(getTemporaryDirectory().getAbsolutePath() + AlbumConstants.THUMBS_TEMP_DIR);
			if (!thumbsDir.exists()) {
				thumbsDir.mkdirs();
			}
		}
		return thumbsDir;
	}
	
	public static File getMidImagesDirectory() {
		if (midImagesDir == null) {
			midImagesDir = new File(getTemporaryDirectory().getAbsolutePath() + AlbumConstants.MID_QUALITY_TEMP_DIR);
			if (!midImagesDir.exists()) {
				midImagesDir.mkdirs();
			}
		}
		return midImagesDir;
	}

}
