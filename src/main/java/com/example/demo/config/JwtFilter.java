package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


//Lo necesitamos para ejecutar el filtro una vez por request
//RESPONSABILIDAD LEER EL TOKEN EN BACKEND QUE TIENE GUARDO EL FRONT EN LOCAL STORAGE
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
    	
    	String path = request.getServletPath();

        if (
            path.endsWith("/usuarios/login") ||
            path.endsWith("/usuarios/add")
           
        ) {

            filterChain.doFilter(request, response);
            return;
        }
    	
        
        
    	//Leer autorizacion header
    	String authHeader =request.getHeader("Authorization");
    	
    	
    	//Comprobar si existe Bearer(Tipo de Token de autenticacion, portador del token para poder acceder)
    	if(authHeader == null ||!authHeader.startsWith( "Bearer ")) {
    		
    			filterChain.doFilter(request, response);

    			   return;
    	}
    	
    	
        String token =authHeader.substring(7);

    	String tel = jwtService.extraerTel(token);
  
    	
    	//CREAR AUTENTICACION
    	String rol =jwtService.extraerRol(token);

    		List<SimpleGrantedAuthority>authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    	 
    		UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken(tel, null, authorities);

          authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));


          // GUARDAR AUTENTICACION
          SecurityContextHolder.getContext().setAuthentication(authToken);
          
          filterChain.doFilter(request,response);

      }
    	
    }









