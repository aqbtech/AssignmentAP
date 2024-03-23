package com.hcmutap.elearning.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class FirebaseConfig {
	@PostConstruct
	public void configureFirebaseConnection() throws FileNotFoundException {
		try {
			File file = ResourceUtils.getFile("classpath:spring-mvc-aa495.json");
			FileInputStream serviceAccount = new FileInputStream(file);
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File not found");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
