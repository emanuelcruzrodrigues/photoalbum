package com.photoalbum.tools;

import java.io.File;
import java.text.Normalizer;

import org.apache.logging.log4j.LogManager;

public class FileNormalizer {
	
	public String normalize(File file) {
		String oldName = file.getAbsolutePath();
		String newName = Normalizer.normalize(oldName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		newName = newName.replace(" ", "_").replace("(", "").replace(")", "");
		
		if (!oldName.equals(newName)) {
			LogManager.getLogger(getClass()).info(String.format("File %s renamed to %s", oldName, newName));
			File newFile = new File(newName);
			if (!newFile.exists()) {
				file.renameTo(newFile);
			}
		}
		
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				normalize(subFile);
			}
		}
		
		return newName;
	}

}
