package com.example.miniproject.config;

import com.example.miniproject.config.jwt.JwtUtil;
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

        String access_token_header = JwtUtil.AUTHORIZATION_HEADER;
        String refresh_token_header = JwtUtil.REFRESHTOKEN_HEADER;

        // 헤더에 security scheme 도 같이 보내게 만드는 것
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(access_token_header).addList(refresh_token_header);

        Components components = new Components()
                .addSecuritySchemes(access_token_header, new SecurityScheme()
                        .name(access_token_header)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER))
                .addSecuritySchemes(refresh_token_header, new SecurityScheme()
                        .name(refresh_token_header)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
