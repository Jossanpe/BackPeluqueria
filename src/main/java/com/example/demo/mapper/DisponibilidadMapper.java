package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.model.dto.DisponibilidadConfiguracionDTO;
import com.example.demo.repository.entity.Disponibilidad;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.DiaSemana;

@Component
public class DisponibilidadMapper {
	
	
	 public Disponibilidad disponibilidadConfiguracionConvertirEntidad(DisponibilidadConfiguracionDTO DisponibilidadConfiguracionDTO, DiaSemana dia, Usuario admin) {
	    	
    	 Disponibilidad disponibilidad = new Disponibilidad();

	        disponibilidad.setUsuarioAdministrador(admin);

	        disponibilidad.setDiaSemana(dia);

	        disponibilidad.setHoraInicio( DisponibilidadConfiguracionDTO.getHoraInicio());

	        disponibilidad.setHoraFin(DisponibilidadConfiguracionDTO.getHoraFin());

	        disponibilidad.setDescansoInicio(DisponibilidadConfiguracionDTO.getDescansoInicio());

	        disponibilidad.setDescansoFin(DisponibilidadConfiguracionDTO.getDescansoFin());

	        disponibilidad.setDuracionSlot(DisponibilidadConfiguracionDTO.getDuracionSlot());

	        disponibilidad.setFechaCreacion(LocalDateTime.now());

	        disponibilidad.setActiva(true);
	        return disponibilidad;
    }
	
	 
	 //CONVERTIR ENTITY A DTO
	public DisponibilidadConfiguracionDTO disponibilidadConfiguracionconvertirDTO(List<Disponibilidad> disponibilidades) {
		
		Disponibilidad primera = disponibilidades.get(0);

		Disponibilidad ultima = disponibilidades.get(disponibilidades.size() - 1);

		DisponibilidadConfiguracionDTO dto = new DisponibilidadConfiguracionDTO();

		dto.setDiaInicio(primera.getDiaSemana().name());

		dto.setDiaFin(ultima.getDiaSemana().name());

		dto.setHoraInicio(primera.getHoraInicio());

		dto.setHoraFin(primera.getHoraFin());

		dto.setDescansoInicio(primera.getDescansoInicio());

		dto.setDescansoFin(primera.getDescansoFin());

		dto.setDuracionSlot(primera.getDuracionSlot());

		return dto;
	}

}
