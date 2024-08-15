package cl.praxis.ReportesInmobiliaria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth// Deshabilitar CSRF de forma actualizada
                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/users/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/roles/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/roles/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/roles/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/reportes/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/reportes/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/reportes/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/reportes/**").hasAuthority("ADMIN")
                                .anyRequest().authenticated()  // Cualquier otra solicitud requiere autenticación

                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Agrega el filtro JWT
                .httpBasic(httpBasic -> {});
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

