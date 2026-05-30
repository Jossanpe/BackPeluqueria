package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.config.SecurityUtils;
import com.example.demo.mapper.ExcepcionDisponibilidadMapper;
import com.example.demo.model.dto.ExcepcionDisponibilidadDTO;
import com.example.demo.repository.dao.ExcepcionDisponibilidadRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.ExcepcionDisponibilidad;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.service.ExcepcionDisponibilidadService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

 @Transactional
@Service
@RequiredArgsConstructor
public class ExcepcionDisponibilidadServiceImpl implements ExcepcionDisponibilidadService {

	private final ExcepcionDisponibilidadRepository excepcionRepository;

	private final UsuarioRepository usuarioRepository;

	
	// CREAR
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@Override
	public ExcepcionDisponibilidadDTO crearExcepcion(ExcepcionDisponibilidadDTO dto) {

		// ADMIN JWT
		String tel =SecurityUtils.obtenerTelUsuario();
		Usuario admin =usuarioRepository.findByTel(tel).orElseThrow();

		// DTO -> ENTITY
		ExcepcionDisponibilidad entity =ExcepcionDisponibilidadMapper.convertToEntity(dto, admin);

		//COMRPOBAR QUE LA HORA DE INCIO NO ES POSTERIOR A LA HORA FINAL
		if( !dto.getDiaCompleto()
			){
			if(
			        dto.getHoraFin()
			        .isBefore(
			            dto.getHoraInicio())
			        ){

			        throw new RuntimeException(
			            "La hora fin no puede ser menor que la hora inicio"
			        );
			    }
			}
		
		// GUARDAR
		entity = excepcionRepository.save(entity);

		// ENTITY -> DTO
		return ExcepcionDisponibilidadMapper.convertToDTO(entity);
	}


	
	// LISTAR
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@Override
	public List<ExcepcionDisponibilidadDTO> obtenerExcepciones() {

		// ADMIN JWT
		String tel = SecurityUtils.obtenerTelUsuario();
		Usuario admin = usuarioRepository.findByTel(tel).orElseThrow();

		// BUSCAR
		List<ExcepcionDisponibilidad> excepciones =	excepcionRepository.findByUsuarioAdministradorOrderByFechaAsc(admin);

		// ENTITY -> DTO convierte automaticamente cada ENTITY en DTO. Stream: flujo iterable, map: transforma cada elemento, collect: vuelve a convertir el streaam en lista
		return excepciones.stream().map(ExcepcionDisponibilidadMapper::convertToDTO).collect(Collectors.toList());
	}

	
	// ELIMINAR
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@Override
	public void eliminarExcepcion(Long idExcepcion) {

		excepcionRepository.deleteById(idExcepcion);
	}


}