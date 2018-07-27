package com.photoalbum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photoalbum.domain.Album;
import com.photoalbum.domain.AlbumPicture;
import com.photoalbum.service.AlbumInitializer;

@RestController
public class AlbumRestController {
	
	@Autowired
	private AlbumInitializer albumInitializer;
	
	@RequestMapping("/rest/picture/{id}")
	public String actionShowPicture(@PathVariable Integer id) {
		
		if (albumInitializer.isRunning()) return "wait";
		
		AlbumPicture picture = Album.getInstance().getAlbumPicture(id);
		
		return picture.getMidImage();
	}

}
