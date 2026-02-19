package com.notemanagement.NoteManagementHubAPI.securities.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        String userMessage;

        // Check the type of exception to customize the message
        if (authException instanceof BadCredentialsException) {
            // Case 1: Login attempt failed due to invalid username/password
            userMessage = "Login Failed: Invalid email or password.";

        } else{
            // Case 2: Missing, Expired, or Malformed Token/Access Attempt
            userMessage = "Login is required.";
        }

        // Log the technical error for the server logs
        logger.warn("Authentication failure at URI: {} - Type: {} - Details: {}",
                request.getRequestURI(), authException.getClass().getSimpleName(), authException.getMessage());

        // Set the response status and content type
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // --- Custom Response Body Construction ---
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("success", false);
        errorDetails.put("message", userMessage);
        errorDetails.put("status", 401);

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
