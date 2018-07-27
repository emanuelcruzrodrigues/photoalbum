package com.photoalbum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.photoalbum.domain.Album;
import com.photoalbum.domain.AlbumPicture;
import com.photoalbum.service.AlbumInitializer;

@RestController
public class AlbumRestController {
	
	@Autowired
	private AlbumInitializer albumInitializer;
	
	@RequestMapping(value = "/rest/picture/{id}", method = RequestMethod.GET, produces = "application/json")
	public String actionGetPicture(Model model, @PathVariable Integer id) {
		
		if (albumInitializer.isRunning()) {
			model.addAttribute("progress", albumInitializer.getProgress());
			return "wait";
		}
		
		AlbumPicture picture = Album.getInstance().getAlbumPicture(id);
		
		String json = picture.getJson();
		return json;
	}

}
