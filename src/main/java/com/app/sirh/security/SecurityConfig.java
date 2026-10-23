package com.app.sirh.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomLoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService, 
                          CustomLoginSuccessHandler loginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Ressources publiques
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/login", "/register", "/offres-emploi").permitAll()
                
                // Rôles et accès sécurisés mis à jour selon ton CustomLoginSuccessHandler
                .requestMatchers("/rh/**").hasRole("MANAGER")
                .requestMatchers("/finance/**").hasRole("MANAGER")
                .requestMatchers("/direction/**").hasRole("MANAGER")
                .requestMatchers("/dept-*/**").hasRole("MANAGER")
                .requestMatchers("/demandes-recrutement/nouvelle").hasRole("MANAGER")
                
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                  
                .loginProcessingUrl("/login")         
                .successHandler(loginSuccessHandler)  
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")    
                .permitAll()
            );

        return http.build();
    }
}