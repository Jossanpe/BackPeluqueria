package com.example.demo.model.dto;



	import java.io.Serializable;
	import java.time.LocalDate;
	import java.time.LocalTime;

	import lombok.Getter;
	import lombok.Setter;

	@Getter
	 @Setter
	public class AgendaSlotDTO implements Serializable {
		
			private static final long serialVersionUID=1L;
		
			private LocalDate fecha;

		    private LocalTime horaInicio;

		    private LocalTime horaFin;
		    private String tipo;


		    private Long idReserva;

		    private String nombreCliente;

		    private String telefonoCliente;

		    private String descripcion;
	}

