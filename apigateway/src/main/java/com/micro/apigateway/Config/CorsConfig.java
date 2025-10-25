package com.micro.apigateway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // ✅ Permitir tu frontend desplegado en Vercel
        corsConfig.setAllowedOrigins(List.of("https://ares-front-end.vercel.app"));

        // ✅ (Opcional) Si quieres permitir también localhost durante desarrollo:
        // corsConfig.setAllowedOrigins(List.of("http://localhost:5173", "https://ares-front-end.vercel.app"));

        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.setAllowCredentials(true); // Muy importante si usas cookies o tokens con credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
