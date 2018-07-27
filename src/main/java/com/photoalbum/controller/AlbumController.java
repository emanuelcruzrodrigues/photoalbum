package com.photoalbum.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.photoalbum.domain.Album;
import com.photoalbum.domain.AlbumDirectory;
import com.photoalbum.domain.AlbumPicture;
import com.photoalbum.service.AlbumInitializer;

@Controller
public class AlbumController {
	
	@Autowired
	private AlbumInitializer albumInitializer;
	
	@RequestMapping(value = "/")
	public String actionShowRoot(Model model) {
		
		if (albumInitializer.isRunning()) {
			model.addAttribute("progress", albumInitializer.getProgress());
			return "wait";
		}
		
		Album album = Album.getInstance();
		
		List<AlbumDirectory> folders = new ArrayList<>(album.getDirectoriesMap().values()).stream().filter(d -> d.getLabel() != null).sorted().collect(Collectors.toList());
		model.addAttribute("folders", folders);
		
		model.addAttribute("randomPicture1", album.getRandomPicture());
		model.addAttribute("randomPicture2", album.getRandomPicture());
		model.addAttribute("randomPicture3", album.getRandomPicture());
		
		return "album";
	}
	
	@RequestMapping(value = "/folder/{id}")
	public String actionShowFolder(Model model, @PathVariable Integer id) {
		
		if (albumInitializer.isRunning()) {
			model.addAttribute("progress", albumInitializer.getProgress());
			return "wait";
		}
		
		AlbumDirectory directory = Album.getInstance().getAlbumDirectory(id);
		
		model.addAttribute("activeFolderPath", directory.getDirectoryPath());
		
		List<AlbumPicture> pictures = directory.getPictures();
		model.addAttribute("pictures", pictures.size() > 0 ? pictures : null);
		
		List<AlbumDirectory> subDirectories = directory.getSubDirectories();
		model.addAttribute("folders", subDirectories.size() > 0 ? subDirectories : null);
		
		return "album";
	}
	
	@RequestMapping(value = "/picture/{id}")
	public String actionShowPicture(Model model, @PathVariable Integer id) {
		
		if (albumInitializer.isRunning()) {
			model.addAttribute("progress", albumInitializer.getProgress());
			return "wait";
		}
		
		AlbumPicture picture = Album.getInstance().getAlbumPicture(id);
		model.addAttribute("picture", picture);
		
		return "picture";
	}
	
	@RequestMapping(value = "/download/{id}")
	public void actionDownload(HttpServletResponse response, @PathVariable Integer id) throws IOException{
		
		AlbumPicture picture = Album.getInstance().getAlbumPicture(id);
		
		File file = new File(picture.getRealPath());
		
		String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            mimeType = "application/octet-stream";
        }
         
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping(value = "/refresh")
	public String actionRefresh(Model model) {
		
		if (!albumInitializer.isRunning()) {
			new Thread(() -> albumInitializer.run()).start();
		}
		
		model.addAttribute("progress", albumInitializer.getProgress());
		return "wait";
	}
	

}
