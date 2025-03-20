package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver CSRF (important pour la console H2)
                .csrf(csrf -> csrf.disable())

                // Configurer CORS pour les origines externes
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configurer les règles d'autorisation
                .authorizeHttpRequests(auth -> auth
                        // Permettre l'accès à la console H2
                        .requestMatchers("/h2-console/**").permitAll()

                        // Permettre l'accès aux endpoints API
                        .requestMatchers("/api/**").permitAll()

                        // Tout le reste nécessite une authentification
                        .anyRequest().authenticated()
                )

                // Autoriser les iframes pour la console H2 avec la nouvelle API
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Origines autorisées
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/*
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configurer CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Désactiver CSRF seulement pour certaines routes
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**"))

                // Configurer les règles d'autorisation
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()  // Console H2 accessible
                        .requestMatchers("/api/**").authenticated()    // Sécuriser les API
                        .anyRequest().authenticated()
                )

                // Autoriser les iframes pour la console H2
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Angular par exemple
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Sécuriser les méthodes HTTP
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

 */
