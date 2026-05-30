package com.example.demo.model.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter

public class ExcepcionDisponibilidadDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idExcepcion;

	private LocalDate fecha;

	private Boolean diaCompleto;

	private LocalTime horaInicio;

	private LocalTime horaFin;
}