package com.notemanagement.NoteManagementHubAPI.securities.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private long expiration;
}
