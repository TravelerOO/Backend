package com.example.miniproject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("MiniProject(Travel SNS)")
                .description("Api Description");

        String access_token_header = "Authorization";
        String refresh_token_header = "RefreshToken";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT AUTH");

        Components components = new Components()
                .addSecuritySchemes(access_token_header, new SecurityScheme()
                        .name(access_token_header)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecuritySchemes(refresh_token_header, new SecurityScheme()
                        .name(refresh_token_header)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
