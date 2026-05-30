package com.example.demo.repository.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.repository.entity.Disponibilidad;
import com.example.demo.repository.entity.Reserva;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.EstadoReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	
	List<Reserva> findByFechaReserva(LocalDate fechaReserva);

	boolean existsByUsuarioClienteAndEstadoReserva(Usuario usuarioCliente, EstadoReserva estadoReserva);

	Optional<Reserva> findByUsuarioClienteAndEstadoReserva(Usuario usuarioCliente, EstadoReserva estadoReserva);
	
	
	
	
	
}
