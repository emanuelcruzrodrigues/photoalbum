package com.photoalbum.domain;

import java.io.File;
import java.util.Comparator;

public class AlbumFileComparator implements Comparator<File> {

	@Override
	public int compare(File file1, File file2) {
		String str1 = String.format("%s_%s", file1.isDirectory() ? "0" : "1", file1.getAbsolutePath());
		String str2 = String.format("%s_%s", file2.isDirectory() ? "0" : "1", file2.getAbsolutePath());
		return str1.compareTo(str2);
	}

}
