package com.example.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.config.SecurityUtils;
import com.example.demo.model.dto.ReservaAdminDTO;
import com.example.demo.model.dto.ReservaConsultaClienteDTO;
import com.example.demo.model.dto.ReservaDTO;
import com.example.demo.model.dto.SlotsDiaDTO;
import com.example.demo.repository.dao.DisponibilidadRepository;
import com.example.demo.repository.dao.ExcepcionDisponibilidadRepository;
import com.example.demo.repository.dao.ReservaRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Disponibilidad;
import com.example.demo.repository.entity.ExcepcionDisponibilidad;
import com.example.demo.repository.entity.Reserva;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.DiaSemana;
import com.example.demo.repository.entity.enums.EstadoReserva;
import com.example.demo.repository.entity.enums.RolUsuario;
import com.example.demo.repository.entity.enums.TipoReserva;
import com.example.demo.service.ReservaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservaServiceImpl implements ReservaService {

	private final DisponibilidadRepository disponibilidadRepository;
	private final ExcepcionDisponibilidadRepository excepcionRepository;
	private final ReservaRepository reservaRepository;

	private final UsuarioRepository usuarioRepository;
	// GENERA LA ESTRUCTURA SEMANAL CON LISTAS VACIAS. PRIMERO MONTAMOS ARQUITECTURA
	// Y LUEGO LLENAMOS LOS SLOTS CON DISPONIBILIDAD, DESCANSOS, EXCEPCIONES Y
	// RESERVAS

	@Override
	public List<SlotsDiaDTO> obtenerSlotsSemana(LocalDate fechaInicioSemana) {

		List<SlotsDiaDTO> resultado = new ArrayList<>();

		// RECORRER 7 DIAS

		for (int i = 0; i < 7; i++) {

			// FECHA ACTUAL
			LocalDate fecha = fechaInicioSemana.plusDays(i);

			// DTO DEL DIA
			SlotsDiaDTO dto = new SlotsDiaDTO();

			dto.setFecha(fecha);

			// CONVERTIR A ENUM
			DiaSemana diaActual = convertirDiaSemana(fecha);

			// BUSCAR DISPONIBILIDAD
			Disponibilidad disponibilidad = disponibilidadRepository.findByDiaSemanaAndActivaTrue(diaActual).orElse(null);

			// SI NO HAY DISPONIBILIDAD DIA VACIO
			if (disponibilidad == null) {
				dto.setSlots(new ArrayList<>());
				resultado.add(dto);
				continue;
			}

			// GENERAR SLOTS BASE
			List<LocalTime> slots = (generarSlots(disponibilidad.getHoraInicio(), disponibilidad.getHoraFin(),
					disponibilidad.getDuracionSlot()));

			// BUSCAR EXCEPCION
			ExcepcionDisponibilidad excepcion = excepcionRepository.findByFecha(fecha).orElse(null);

			// APLICAR EXCEPCION
			if (excepcion != null) {

				// DIA COMPLETO
				if (excepcion.getDiaCompleto()) {

					slots = new ArrayList<>();

				} else {
					// EXCEPCION HORARIA
					slots = filtrarExcepcion(slots, excepcion.getHoraInicio(), excepcion.getHoraFin());
				}
			}

			// FILTRAR RESERVAS

			List<Reserva> reservas = reservaRepository.findByFechaReserva(fecha);
			slots = filtrarReservas(slots, reservas);

			// FILTRAR DESCANSO
			if (disponibilidad.getDescansoInicio() != null && disponibilidad.getDescansoFin() != null) {

				slots = filtrarDescanso(slots, disponibilidad.getDescansoInicio(), disponibilidad.getDescansoFin());

			}
			// GUARDAR SLOTS

			dto.setSlots(slots);

			resultado.add(dto);
		}

		return resultado;

	}

	// FILTRAR DESCANSO- //PARA ELIMINAR LOS SLOTS DENTRO DEL DESCANSO
	private List<LocalTime> filtrarDescanso(List<LocalTime> slots, LocalTime descansoInicio, LocalTime descansoFin) {

		return slots.stream().filter(slot -> slot.isBefore(descansoInicio) || !slot.isBefore(descansoFin)).toList();

	}

	// GENERA SLOTS

	private List<LocalTime> generarSlots(LocalTime inicio, LocalTime fin, Integer duracion) {

		List<LocalTime> slots = new ArrayList<>();

		LocalTime actual = inicio;

		while (actual.isBefore(fin)) {

			slots.add(actual);

			actual = actual.plusMinutes(duracion); // va sumando la duracion constantemente hasta que llega a la hora
													// indicada como fin. Asi añade todos los slots necesarios.
		}

		return slots;
	}

	// FILTRAR EXCEPCIONES
	private List<LocalTime> filtrarExcepcion(List<LocalTime> slots, LocalTime inicio, LocalTime fin) {

		return slots.stream().filter(slot -> slot.isBefore(inicio) || !slot.isBefore(fin)).toList();
	}

	// FILTRAR RESERVAS, ELIMINA SLOTS YA RESERVADOS
	private List<LocalTime> filtrarReservas(List<LocalTime> slots, List<Reserva> reservas) {

		return slots.stream().filter(slot -> reservas.stream().noneMatch(
						reserva -> !slot.isBefore(reserva.getHoraInicio()) && slot.isBefore(reserva.getHoraFin()))).toList();
	}

	// CONVERTIR DIA SEMANA

	private DiaSemana convertirDiaSemana(LocalDate fecha) {

		return DiaSemana.values()[fecha.getDayOfWeek().getValue() - 1];
	}


	
	
	
	

	@Override
	public void crearReserva(ReservaDTO dto) {

		String tel = SecurityUtils.obtenerTelUsuario();

		Usuario cliente = usuarioRepository.findByTel(tel).orElseThrow();

		Usuario admin = usuarioRepository.findFirstByRolUsuario(RolUsuario.ADMINISTRADOR).orElseThrow();

		crearReservaInterna(cliente, admin, dto.getFechaReserva(), dto.getHoraInicio(), dto.getDuracionMinutos(),
				TipoReserva.CLIENTE);
		}



	@PreAuthorize("hasRole('ADMINISTRADOR')")
		@Override
		public void crearReservaAdmin(ReservaAdminDTO dto) {

			Usuario cliente = null;

			if (dto.getIdCliente() != null) {

				cliente = usuarioRepository.findById(dto.getIdCliente()).orElseThrow();
			}

			Usuario admin = usuarioRepository.findByTel(SecurityUtils.obtenerTelUsuario()).orElseThrow();
			
			
			crearReservaInterna(cliente, admin, dto.getFechaReserva(), dto.getHoraInicio(), dto.getDuracionMinutos(),
					TipoReserva.ADMINISTRADOR);

		  }
		
	
		
		
		
			private void crearReservaInterna(Usuario cliente, Usuario admin, LocalDate fecha, LocalTime horaInicio,
					Integer duracion, TipoReserva tipoReserva) {

				if (duracion == null || duracion <= 0) {

					DiaSemana diaSemana = convertirDiaSemana(fecha);

					Disponibilidad disponibilidad = disponibilidadRepository.findByDiaSemanaAndActivaTrue(diaSemana).orElseThrow();

					duracion = disponibilidad.getDuracionSlot();
				}
				LocalTime horaFin = horaInicio.plusMinutes(duracion);

				List<Reserva> reservas = reservaRepository.findByFechaReserva(fecha);

				boolean existeSolape = reservas.stream().anyMatch(reserva -> horaInicio.isBefore(reserva.getHoraFin())
						&& horaFin.isAfter(reserva.getHoraInicio()));

				if (existeSolape) {
					throw new RuntimeException("Ya existe una reserva en ese horario");
				}

				Reserva reserva = new Reserva();

				reserva.setUsuarioCliente(cliente);
				reserva.setUsuarioAdministrador(admin);

				reserva.setFechaReserva(fecha);

				reserva.setHoraInicio(horaInicio);

				reserva.setHoraFin(horaFin);

				reserva.setDuracionMinutos(duracion);

				reserva.setEstadoReserva(EstadoReserva.ACTIVA);

				reserva.setTipoReserva(tipoReserva);
				
				if (cliente != null && reservaRepository.existsByUsuarioClienteAndEstadoReserva(cliente, EstadoReserva.ACTIVA)) {

					throw new RuntimeException("Ya tienes una reserva activa");
				}

				reservaRepository.save(reserva);
			}

			
			
			
			@Override
			public ReservaConsultaClienteDTO obtenerReservaCliente() {
				String tel = SecurityUtils.obtenerTelUsuario();


			    Usuario cliente = usuarioRepository.findByTel(tel).orElseThrow();
			    
				Reserva reserva = reservaRepository.findByUsuarioClienteAndEstadoReserva(cliente, EstadoReserva.ACTIVA).orElse(null);
				if(reserva == null) {

				    return null;
				}
				    ReservaConsultaClienteDTO dto = new ReservaConsultaClienteDTO();
					dto.setIdReserva(reserva.getIdReserva());

					dto.setFechaReserva(reserva.getFechaReserva());

					dto.setHoraInicio(reserva.getHoraInicio());

					dto.setTipoReserva(reserva.getTipoReserva());
					return dto;
				    
				}
			
			
			@Override
			public void cancelarReserva() {
				String tel = SecurityUtils.obtenerTelUsuario();

				Usuario cliente = usuarioRepository.findByTel(tel).orElseThrow();

				Reserva reserva = reservaRepository.findByUsuarioClienteAndEstadoReserva(cliente, EstadoReserva.ACTIVA).orElseThrow();
				reserva.setEstadoReserva(EstadoReserva.CANCELADA);

			}
			
			
			
			

			}
