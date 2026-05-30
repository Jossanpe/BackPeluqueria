package com.example.demo.repository.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name = "excepcion_disponibilidad")
public class ExcepcionDisponibilidad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name = "id_excepcion")
	private Long idExcepcion;

	// ADMIN
	@ManyToOne
	@JoinColumn(name = "id_usuario_administrador",nullable = false)
	private Usuario usuarioAdministrador;

	// FECHA
	@Column(nullable = false)
	private LocalDate fecha;

	// DIA COMPLETO
	@Column(name = "dia_completo",nullable = false)
	private Boolean diaCompleto;

	// HORA INICIO
	@Column(name = "hora_inicio")
	private LocalTime horaInicio;

	// HORA FIN
	@Column(name = "hora_fin")
	private LocalTime horaFin;

	// FECHA CREACION
	@Column(name = "fecha_creacion",nullable = false)
	private LocalDateTime fechaCreacion;
}