package com.example.demo.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReservaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    private LocalDate fechaReserva;

    private LocalTime horaInicio;
    
    private int duracionMinutos;
}