package com.example.demo.mapper;
import java.time.LocalDateTime;
import com.example.demo.model.dto.ExcepcionDisponibilidadDTO;
import com.example.demo.repository.entity.ExcepcionDisponibilidad;
import com.example.demo.repository.entity.Usuario;


public class ExcepcionDisponibilidadMapper {

	// DTO -> ENTITY

	public static ExcepcionDisponibilidad convertToEntity(ExcepcionDisponibilidadDTO dto,Usuario admin) {

		ExcepcionDisponibilidad excepcion =new ExcepcionDisponibilidad();

		excepcion.setUsuarioAdministrador(admin);

		excepcion.setFecha(dto.getFecha());

		excepcion.setDiaCompleto(dto.getDiaCompleto());

		excepcion.setHoraInicio(dto.getHoraInicio());

		excepcion.setHoraFin(dto.getHoraFin());

		excepcion.setFechaCreacion(LocalDateTime.now());

		return excepcion;
	}

	// ENTITY -> DTO

	public static ExcepcionDisponibilidadDTO convertToDTO(ExcepcionDisponibilidad entity) {ExcepcionDisponibilidadDTO dto = new ExcepcionDisponibilidadDTO();

		dto.setIdExcepcion(entity.getIdExcepcion());

		dto.setFecha(entity.getFecha());

		dto.setDiaCompleto(entity.getDiaCompleto());

		dto.setHoraInicio(entity.getHoraInicio());

		dto.setHoraFin(entity.getHoraFin());

		return dto;
	}
}