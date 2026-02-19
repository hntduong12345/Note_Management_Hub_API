package com.notemanagement.NoteManagementHubAPI.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**") // Apply to all API endpoints starting with /api/
                .allowedOrigins("http://localhost:3000") // Specify allowed domains
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Allow common HTTP methods
                .allowedHeaders("*") // Allow all headers (including Authorization for JWT)
                .allowCredentials(true) // Allow cookies or authentication headers (like JWT)
                .maxAge(3600); // How long to cache the CORS preflight response
    }
}
