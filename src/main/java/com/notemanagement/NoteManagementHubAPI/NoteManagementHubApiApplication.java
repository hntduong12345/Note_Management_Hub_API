package com.notemanagement.NoteManagementHubAPI;

import com.notemanagement.NoteManagementHubAPI.models.User;
import com.notemanagement.NoteManagementHubAPI.securities.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class NoteManagementHubApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteManagementHubApiApplication.class, args);
	}
}
