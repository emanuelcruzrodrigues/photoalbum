package com.photoalbum.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.photoalbum.service.AlbumService;

@Controller
public class AlbumController {
	
	@Autowired
	private AlbumService albumService;
	
	@RequestMapping(value = "/")
	public String actionShowRoot(Model model) {
		
		model.addAttribute("activeFolder", "/");
		
		Map<String, String> directoriesMap = albumService.getSubFolders();
		if (directoriesMap.size() > 0) {
			model.addAttribute("folders", directoriesMap);
		}
		
		return "album";
	}
	
	@RequestMapping(value = "/folder/{path}")
	public String actionShowFolder(Model model, @PathVariable String path) {
		
		String decodedPath = albumService.decodePath(path);
		
		String activeFolder = decodedPath.replace("/", " / ");
		model.addAttribute("activeFolder", activeFolder);
		
		Map<String, String> pictures = albumService.getThumbsPaths(decodedPath);
		if (pictures.size() > 0) {
			model.addAttribute("pictures", pictures);
		}
		
		Map<String, String> directoriesMap = albumService.getSubFolders(decodedPath);
		if (directoriesMap.size() > 0) {
			model.addAttribute("folders", directoriesMap);
		}
		
		return "album";
	}
	
	@RequestMapping(value = "/picture/{path}")
	public String actionShowPicture(Model model, @PathVariable String path) {
		
		String thumbPath = albumService.decodePath(path);
		
		StringBuilder albumPath = new StringBuilder();
		String[] thumbPathSplitted = path.split("@");
		for (int i = 1; i < thumbPathSplitted.length-1; i++) {
			if (albumPath.length() > 0) albumPath.append("@");
			albumPath.append(thumbPathSplitted[i]);
		}
		model.addAttribute("albumPath", "/folder/" + albumPath.toString());
		
		String picturePath = thumbPath.replace("_thumb", "");
		model.addAttribute("picturePath", picturePath);
		
		String[] split = picturePath.split("/");
		String pictureName = split[split.length-1];
		model.addAttribute("pictureName", pictureName);
		
		String[] priorAndNextPictures = albumService.getPriorAndNextPicturesPaths(albumService.decodePath(albumPath.toString()), pictureName);
		
		String priorPicture = priorAndNextPictures[0];
		if (priorPicture != null) {
			model.addAttribute("priorPicture", "/picture/" + priorPicture);
		}
		
		String nextPicture = priorAndNextPictures[1];
		if (nextPicture != null) {
			model.addAttribute("nextPicture", "/picture/" + nextPicture);
		}
		
		
		return "picture";
	}

}
