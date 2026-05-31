package com.example.demo.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.repository.entity.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReservaAdminDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	 private String telCliente;

    private LocalDate fechaReserva;

    private LocalTime horaInicio;
    private LocalTime horaFin;  
    private int duracionMinutos;
    private String descripcion;
}