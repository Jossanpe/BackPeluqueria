package com.example.demo.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.repository.entity.Disponibilidad;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.DiaSemana;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisponibilidadConfiguracionDTO implements Serializable {
	private static final long serialVersionUID=1L;
	
	 private String diaInicio;

	    private String diaFin;

	    private LocalTime horaInicio;

	    private LocalTime horaFin;

	    private LocalTime descansoInicio;

	    private LocalTime descansoFin;

	    private Integer duracionSlot;
	    
	    
	    
	    
	   

}
