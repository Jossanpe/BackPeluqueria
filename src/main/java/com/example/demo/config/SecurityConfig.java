package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;

	//Crear un objeto automaticamente y lo guarda para reutilizarlo
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    
    
    //Estas linea permiten el acceso publicon sin autorizacion al registro y al login. RquestMatchers son rutas publicas. SE PUEDE ACCEDER A ESTAS RUTAS SIN TOKEN.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//PermitAll() no impide que el filtro se ejecute
        http.cors(cors->{}).csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.requestMatchers("/**/usuarios/add", "/**/usuarios/login","/uploads/**").permitAll()
        		.requestMatchers("/admin/**").hasRole("ADMINISTRADOR").anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
    
    
    //PERMITE AL SERVIDOR DEL FRONT CONECTARSE AL BACK. CORS!
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("http://*.localhost:4200" ));

        configuration.setAllowedMethods( List.of("*") );

        configuration.setAllowedHeaders( List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration );

        return source;
    }

}