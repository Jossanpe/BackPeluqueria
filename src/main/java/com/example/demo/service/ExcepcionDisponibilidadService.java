package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.ExcepcionDisponibilidadDTO;


public interface ExcepcionDisponibilidadService {

	// CREAR
	ExcepcionDisponibilidadDTO crearExcepcion(ExcepcionDisponibilidadDTO dto);

	// LISTAR
	List<ExcepcionDisponibilidadDTO> obtenerExcepciones();

	// ELIMINAR
	void eliminarExcepcion(Long idExcepcion);
}