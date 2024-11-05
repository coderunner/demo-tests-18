package org.inf5190.library;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String allowedOrigins = Optional.ofNullable(System.getenv("ALLOWED_ORIGINS"))
                .orElse("http://127.0.0.1:4200,http://localhost:4200");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods("GET", "POST", "DELETE");
            }
        };
    }
}
