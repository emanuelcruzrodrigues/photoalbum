package com.photoalbum.domain;

import java.io.File;

public class FileWrapper implements Comparable<FileWrapper>{
	
	private Integer id;
	private String name;
	private String label;
	private String realPath;
	
	public FileWrapper(String label, File file) {
		super();
		this.label = label;
		this.name = file.getName();
		this.realPath = file.getAbsolutePath();
		this.id = realPath.hashCode();
	}

	public Integer getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return label != null ? label : name;
	}

	public String getRealPath() {
		return realPath;
	}
	
	@Override
	public String toString() {
		return realPath;
	}

	@Override
	public int compareTo(FileWrapper o) {
		return getRealPath().compareTo(o.getRealPath());
	}

}
