package com.example.demo.repository.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.repository.entity.enums.EstadoReserva;
import com.example.demo.repository.entity.enums.TipoReserva;

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


@Setter
@Getter
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "id_reserva")
    private Long idReserva;

    
    // CLIENTE QUE RESERVA
    @ManyToOne
    @JoinColumn(name = "id_usuario_cliente", nullable = true)
    private Usuario usuarioCliente;


    // ADMINISTRADOR QUE GESTIONA
    @ManyToOne
    @JoinColumn(name = "id_usuario_administrador")
    private Usuario usuarioAdministrador;


    // FECHA RESERVA
    @Column(name ="fecha", nullable = false)
    private LocalDate fechaReserva;


    // HORA INICIO
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;


    // HORA FIN
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;


    // TIPO RESERVA
    @Enumerated(EnumType.STRING)

    @Column(name = "tipo_reserva", nullable = false)

    private TipoReserva tipoReserva;

    // ESTADO RESERVA
    @Enumerated(EnumType.STRING)

    @Column(name = "estado_reserva", nullable = false)

    private EstadoReserva estadoReserva;

    // DESCRIPCIÓN
    @Column(length = 500)
    private String descripcion;
    
    //DURACION
    @Column(name="duracion_minutos")
    private int duracionMinutos;


  
}

