package com.example.demo.repository.entity;


import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.repository.entity.enums.DiaSemana;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "disponibilidad_publica")

public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_disponibilidad")
    private Long idDisponibilidad;


    // ADMINISTRADOR
    @ManyToOne
    @JoinColumn(name = "id_usuario_administrador", nullable = false)

    private Usuario usuarioAdministrador;


    // DÍA SEMANA
    @Enumerated(EnumType.STRING)

    @Column(name = "dia_semana", nullable = false)

    private DiaSemana diaSemana;


    // HORA INICIO
    @Column(name = "hora_inicio", nullable = false)

    private LocalTime horaInicio;


    // HORA FIN
    @Column(name = "hora_fin", nullable = false)

    private LocalTime horaFin;
    
    
    //HORA INICIO DESCANSO
    @Column(name = "descanso_inicio")

    private LocalTime descansoInicio;

    

    //HORA INICIO DESCANSO
    @Column(name = "descanso_fin")

    private LocalTime descansoFin;


    // DURACIÓN SLOT
    @Column(name = "duracion_slot", nullable = false)

    private Integer duracionSlot;


    // DISPONIBILIDAD ACTIVA
    @Column(nullable = false)

    private Boolean activa = true;


    // FECHA CREACIÓN
    @Column(name = "fecha_creacion", nullable = false)

    private LocalDateTime fechaCreacion;


    // FECHA INACTIVACIÓN
    @Column(name = "fecha_inactivacion")

    private LocalDateTime fechaInactivacion;
    
 
}