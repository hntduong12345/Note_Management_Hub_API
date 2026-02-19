package com.notemanagement.NoteManagementHubAPI.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customeOpenAPI(){
        final String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .addSecurityItem((new SecurityRequirement().addList(securitySchemeName)))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Paste your token here (WITHOUT prefix Bearer).")))
                .info(new Info()
                        .title("**Note Management Hub API**")
                        .version("1.0")
                        .description("API documentation for simple note management hub project.")
                        .contact(new Contact()
                                .name("Test")
                                .email("test@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                );
    }
}
