package com.suivi_academique.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint; // Import nécessaire
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    // 🔑 AJOUT CLÉ 1: Injection du bean AuthenticationEntryPoint
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver le CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Configurer le CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Définir les autorisations
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // Chemins publics : authentification, documentation Swagger
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/personnel",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Tout autre chemin REQUIERT une authentification
                        .anyRequest().authenticated()
                )
                // Configurer la gestion des sessions (STATELESS pour les JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 🔑 AJOUT CLÉ 2: Configurer le gestionnaire d'exceptions pour les requêtes non authentifiées
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint) // Utilisation du bean 401
                )
                .authenticationProvider(authenticationProvider)
                // Ajouter le filtre JWT avant le filtre d'authentification standard
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(java.util.List.of("*"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}