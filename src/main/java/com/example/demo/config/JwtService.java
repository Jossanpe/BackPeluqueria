package com.example.demo.config;

//ESTA CLASE TIENE LA RESPONSABILIDAD DE LEER EL TELEFONO, EL ROL, EL TENANT O LOS ATRIBUTOS QUE HAYAMOS PUESTO Y SE ENCUENTRAN EN EL TOKEN
import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;


//Expiración una hora. issue=fecha creacion, CurrentTimeMillis es el tiempo actual mas 1 hora.1000=1milisegundo; x 60= 1 minuto; x60= 1 hora
    public String generarToken(String tel, String rol, String tenant) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        				
        return Jwts.builder().subject(tel).claim("rol", rol).claim("tenant", tenant).issuedAt(new Date())
        		.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )).signWith(key).compact();

    }
    
    
    
    public String extraerTel(String token) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();

    }
    
    public String extraerRol(String token) { SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser().verifyWith(key) .build() .parseSignedClaims(token) .getPayload().get("rol", String.class);

    }
    
    
    public String extraerTenant(String token) {SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("tenant", String.class);

    }
    
    

}