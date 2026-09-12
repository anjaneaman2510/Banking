package com.b.bank.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AdminConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain securityChain2(HttpSecurity h1) throws Exception {
        h1
            .securityMatcher("/admin/**")
            .cors(cors -> cors.configurationSource(corsConfigurationSource1()))
            .csrf(csrf -> csrf.disable())
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                 .requestMatchers("/admin/loginByMobile").permitAll()
                // .requestMatchers("/admin/getByAccount").permitAll()
                .anyRequest().authenticated()
            )
                         .oauth2ResourceServer(oauth2 ->
    oauth2.jwt(Customizer.withDefaults())
);


        return h1.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource1() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS" , "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        
        // Browser ko popup open karne ke liye ye header expose karna anivarya hai
        config.setExposedHeaders(List.of("WWW-Authenticate"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder1() {
        return NoOpPasswordEncoder.getInstance();
    }
}
