package com.photoalbum.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.photoalbum.service.AlbumInitializer;

@Component
public class ApplicationStarter {

	@Autowired
	private AlbumInitializer albumInitializer;
	
	@PostConstruct
	public void start() {
		new Thread(() -> albumInitializer.run()).start();
	}
	
}
