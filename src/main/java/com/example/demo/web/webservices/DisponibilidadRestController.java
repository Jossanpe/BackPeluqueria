package com.example.demo.web.webservices;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.DisponibilidadConfiguracionDTO;
import com.example.demo.service.DisponibilidadService;

import lombok.RequiredArgsConstructor;

@RestController

@RequestMapping("/disponibilidad")

@RequiredArgsConstructor
public class DisponibilidadRestController {
	

	    private final DisponibilidadService disponibilidadService;

	    // GUARDAR DISPONIBILIDAD
	    @PreAuthorize("hasRole('ADMINISTRADOR')")
	    @PostMapping
	    public ResponseEntity<DisponibilidadConfiguracionDTO>

	    guardarDisponibilidad( @RequestBody DisponibilidadConfiguracionDTO dto){
	    	DisponibilidadConfiguracionDTO respuesta = disponibilidadService.guardarDisponibilidad(dto);
	        return ResponseEntity.ok(respuesta);
	    }
	    
	    

	    // OBTENER DISPONIBILIDAD
	    @PreAuthorize("hasRole('ADMINISTRADOR')")
	    @GetMapping
	    public ResponseEntity<DisponibilidadConfiguracionDTO>obtenerDisponibilidad(){

	        DisponibilidadConfiguracionDTO respuesta = disponibilidadService.obtenerDisponibilidad();

	        return ResponseEntity.ok(respuesta);
	    }
	}


