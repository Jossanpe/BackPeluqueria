package com.example.demo.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.repository.entity.enums.TipoReserva;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaConsultaClienteDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	 private Long idReserva;

	    private LocalDate fechaReserva;

	    private LocalTime horaInicio;

	    private TipoReserva tipoReserva;
	}