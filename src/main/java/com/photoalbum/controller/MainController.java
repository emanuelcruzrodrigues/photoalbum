package com.photoalbum.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.photoalbum.config.AlbumDirectories;
import com.photoalbum.service.AlbumService;

@Controller
public class MainController {
	
	@Autowired
	private AlbumService albumService;
	
	@RequestMapping(value = "/login")
	public String actionLogin(Model model) {
		return "login";
	}
	
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
		String label = AlbumDirectories.getLabel(decodedPath);
		String root = AlbumDirectories.getDirectory(label);
		if (root.endsWith("/")) root = root.substring(0, root.length()-1);
		String activeFolder = decodedPath.replace(root, label).replace("/", " / ");
		model.addAttribute("activeFolder", activeFolder);
		
		Map<String, String> pictures = albumService.getPictures(path);
		if (pictures.size() > 0) {
			model.addAttribute("pictures", pictures);
		}
		
		Map<String, String> directoriesMap = albumService.getSubFolders(path);
		if (directoriesMap.size() > 0) {
			model.addAttribute("folders", directoriesMap);
		}
		
		return "album";
	}
	
	
	
}
