package Adventure_generator.Config;

/**
 * Security Configuration
 * 
 * Spring Security configuration for JWT-based authentication.
 * Defines security filter chain, public/protected endpoints, and JWT filter integration.
 * 
 * Security Features:
 * - JWT token validation filter
 * - CSRF protection disabled (using JWT)
 * - Role-based access control
 * - Public endpoints for login/register
 * - Protected endpoints for authenticated users
 * 
 * Public Endpoints:
 * - /api/auth/** - Authentication endpoints
 * - /api/weather - Weather data (requires JWT)
 * - /login.html, /home.html - Static pages
 * - /styles.css, /js/** - Static resources
 * 
 * @author Astra K. Nguyen
 * @version 1.0.0
 * @since 2026-01-28
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/login.html", "/home.html", "/styles.css", "/js/**", "/").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }


}
