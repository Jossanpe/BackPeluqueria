package com.example.demo.web.webservices;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ExcepcionDisponibilidadDTO;

import com.example.demo.service.ExcepcionDisponibilidadService;

import lombok.RequiredArgsConstructor;

@RestController

@RequestMapping("/disponibilidad/excepciones")

@RequiredArgsConstructor

public class ExcepcionDisponibilidadRestController {

	private final ExcepcionDisponibilidadService excepcionService;

	// CREAR
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping
	public ExcepcionDisponibilidadDTO crearExcepcion(@RequestBody ExcepcionDisponibilidadDTO dto) {

		return excepcionService.crearExcepcion(dto);
	}

	
	
	// LISTAR
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@GetMapping
	public List<ExcepcionDisponibilidadDTO> obtenerExcepciones() {

		return excepcionService.obtenerExcepciones();
	}

	
	
	
	// ELIMINAR
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/{idExcepcion}")
	public void eliminarExcepcion(@PathVariable Long idExcepcion) {

		excepcionService.eliminarExcepcion(idExcepcion);
	}
}





