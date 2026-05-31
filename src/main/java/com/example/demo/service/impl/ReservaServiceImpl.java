package com.example.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.config.SecurityUtils;
import com.example.demo.model.dto.AgendaSlotDTO;
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

	
	
	
	//OBTNER SLOTS DE LA SEMANA
	@Override
	public List<SlotsDiaDTO> obtenerSlotsSemana(LocalDate fechaInicioSemana) {
		String tel = SecurityUtils.obtenerTelUsuario();

		Usuario cliente = usuarioRepository.findByTel(tel).orElseThrow();

		Usuario admin = usuarioRepository.findByRolUsuarioAndTenant(RolUsuario.ADMINISTRADOR, cliente.getTenant())
				.orElseThrow();
		actualizarReservasCompletadas(admin);

		List<SlotsDiaDTO> resultado = new ArrayList<>();

		// recorrer 7 dias

		for (int i = 0; i < 7; i++) {

			// fecha actual
			LocalDate fecha = fechaInicioSemana.plusDays(i);

			// dto del dia
			SlotsDiaDTO dto = new SlotsDiaDTO();

			dto.setFecha(fecha);

			// convertir a enum
			DiaSemana diaActual = convertirDiaSemana(fecha);

			// buscar disponibilidad
			Disponibilidad disponibilidad = disponibilidadRepository
					.findByUsuarioAdministradorAndDiaSemanaAndActivaTrue(admin, diaActual).orElse(null);

			// si no hay disponibilidad dia vacio
			if (disponibilidad == null) {
				dto.setSlots(new ArrayList<>());
				resultado.add(dto);
				continue;
			}

			// generar slots base
			List<LocalTime> slots = (generarSlots(disponibilidad.getHoraInicio(), disponibilidad.getHoraFin(),
					disponibilidad.getDuracionSlot()));

			// buscar excepcion
			List<ExcepcionDisponibilidad> excepciones = excepcionRepository.findByUsuarioAdministradorAndFecha(admin,
					fecha);

			// aplicar excepcion
			for (ExcepcionDisponibilidad excepcion : excepciones) {

				if (Boolean.TRUE.equals(excepcion.getDiaCompleto())) {

					slots = new ArrayList<>();

					break;
				}

				slots = filtrarExcepcion(slots, excepcion.getHoraInicio(), excepcion.getHoraFin());
			}

			// FILTRAR RESERVAS

			List<Reserva> reservas = reservaRepository.findByUsuarioAdministradorAndFechaReservaAndEstadoReserva(admin, fecha,EstadoReserva.ACTIVA);
			slots = filtrarReservas(slots, reservas);

			//filtrar descanso
			if (disponibilidad.getDescansoInicio() != null && disponibilidad.getDescansoFin() != null) {

				slots = filtrarDescanso(slots, disponibilidad.getDescansoInicio(), disponibilidad.getDescansoFin());

			}
			// guardar slots

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

		return slots.stream()
				.filter(slot -> reservas.stream().noneMatch(
						reserva -> !slot.isBefore(reserva.getHoraInicio()) && slot.isBefore(reserva.getHoraFin())))
				.toList();
	}
	
	

	// CONVERTIR DIA SEMANA

	private DiaSemana convertirDiaSemana(LocalDate fecha) {

		return DiaSemana.values()[fecha.getDayOfWeek().getValue() - 1];
	}

	
	
	
	//CREAR RESERVA
	
	@Override
	public void crearReserva(ReservaDTO dto) {

		String tel = SecurityUtils.obtenerTelUsuario();

		Usuario cliente = usuarioRepository.findByTel(tel).orElseThrow();

		Usuario admin = usuarioRepository.findByRolUsuarioAndTenant(RolUsuario.ADMINISTRADOR, cliente.getTenant())
				.orElseThrow();

		crearReservaInterna(cliente, admin, dto.getFechaReserva(), dto.getHoraInicio(), dto.getDuracionMinutos(),dto.getDescripcion(),
				TipoReserva.CLIENTE);
	}

	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@Override
	public void crearReservaAdmin(ReservaAdminDTO dto) {
		Usuario admin = usuarioRepository.findByTel(SecurityUtils.obtenerTelUsuario()).orElseThrow();
		Usuario cliente = null;

		if (dto.getTelCliente() != null && !dto.getTelCliente().isBlank()) {

			cliente = usuarioRepository.findByTelAndTenant(dto.getTelCliente(), admin.getTenant()).orElseThrow();
		}

		crearReservaInterna(cliente, admin, dto.getFechaReserva(), dto.getHoraInicio(), dto.getDuracionMinutos(),dto.getDescripcion(),
				TipoReserva.ADMINISTRADOR);

	}

	
	
	private void crearReservaInterna(Usuario cliente, Usuario admin, LocalDate fecha, LocalTime horaInicio,
			Integer duracion,String descripcion, TipoReserva tipoReserva) {

		if (duracion == null || duracion <= 0) {

			DiaSemana diaSemana = convertirDiaSemana(fecha);

			Disponibilidad disponibilidad = disponibilidadRepository
					.findByUsuarioAdministradorAndDiaSemanaAndActivaTrue(admin, diaSemana).orElseThrow();

			duracion = disponibilidad.getDuracionSlot();
		}
		LocalTime horaFin = horaInicio.plusMinutes(duracion);

		List<Reserva> reservas = reservaRepository.findByUsuarioAdministradorAndFechaReservaAndEstadoReserva(admin, fecha, EstadoReserva.ACTIVA);

		boolean existeSolape = reservas.stream().anyMatch(
				reserva -> horaInicio.isBefore(reserva.getHoraFin()) && horaFin.isAfter(reserva.getHoraInicio()));

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

		if (cliente != null
				&& reservaRepository.existsByUsuarioClienteAndEstadoReserva(cliente, EstadoReserva.ACTIVA)) {

			throw new RuntimeException("Ya tienes una reserva activa");
		}

		reservaRepository.save(reserva);
	}

	@Override
	public ReservaConsultaClienteDTO obtenerReservaCliente() {
		String tel = SecurityUtils.obtenerTelUsuario();

		Usuario cliente = usuarioRepository.findByTel(tel).orElseThrow();
		actualizarReservasCompletadas(cliente);

		Reserva reserva = reservaRepository.findByUsuarioClienteAndEstadoReserva(cliente, EstadoReserva.ACTIVA)
				.orElse(null);
		if (reserva == null) {

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

		Reserva reserva = reservaRepository.findByUsuarioClienteAndEstadoReserva(cliente, EstadoReserva.ACTIVA)
				.orElseThrow();
		reserva.setEstadoReserva(EstadoReserva.CANCELADA);

	}
	
	@Override
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public void cancelarReservaAdmin(Long idReserva) {

		Usuario admin = usuarioRepository.findByTel(SecurityUtils.obtenerTelUsuario()).orElseThrow();

		Reserva reserva = reservaRepository.findByIdReservaAndUsuarioAdministrador(idReserva, admin).orElseThrow();

		reserva.setEstadoReserva(EstadoReserva.CANCELADA);
	}
	
	
	

	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@Override
	public List<AgendaSlotDTO> obtenerAgendaSemana(LocalDate fechaInicioSemana) {

		Usuario admin = usuarioRepository.findByTel(SecurityUtils.obtenerTelUsuario()).orElseThrow();
		actualizarReservasCompletadas(admin);

		List<Disponibilidad> disponibilidades = disponibilidadRepository.findByUsuarioAdministradorAndActivaTrue(admin);

		if (disponibilidades.isEmpty()) {
			throw new RuntimeException("No existe disponibilidad configurada");
		}

		Integer duracionSlot = disponibilidades.get(0).getDuracionSlot();
		
		List<Reserva> reservas = reservaRepository.findByUsuarioAdministradorAndFechaReservaBetween(admin,fechaInicioSemana, fechaInicioSemana.plusDays(6));
		
		List<ExcepcionDisponibilidad> excepciones = excepcionRepository.findByUsuarioAdministradorOrderByFechaAsc(admin);
		
		

		List<AgendaSlotDTO> agenda = new ArrayList<>();

		for (int dia = 0; dia < 7; dia++) {

			LocalDate fecha = fechaInicioSemana.plusDays(dia);
			
			DiaSemana diaSemana = convertirDiaSemana(fecha);

			Disponibilidad disponibilidad = disponibilidadRepository.findByUsuarioAdministradorAndDiaSemanaAndActivaTrue(admin, diaSemana).orElse(null);

			LocalTime hora = LocalTime.of(8, 0);

			while (hora.isBefore(LocalTime.of(23, 0))) {

				AgendaSlotDTO slot = new AgendaSlotDTO();

				slot.setFecha(fecha);

				slot.setHoraInicio(hora);

				slot.setHoraFin(hora.plusMinutes(duracionSlot));
				slot.setTipo("LIBRE");
				
				LocalTime horaActual = hora;
				Reserva reserva = reservas.stream().filter(r -> r.getEstadoReserva() == EstadoReserva.ACTIVA && r.getFechaReserva().equals(fecha)
								&& r.getHoraInicio().equals(horaActual)).findFirst().orElse(null);
				
				if (reserva != null) {


				    if (reserva.getUsuarioCliente()==null) {

				        slot.setTipo("BLOQUEO_ADMIN");

				    } else {

				        slot.setTipo("RESERVA_CLIENTE");
				    }

					slot.setIdReserva(reserva.getIdReserva());
					slot.setDescripcion(reserva.getDescripcion());
					
					if (reserva.getUsuarioCliente() != null) {
						
						slot.setNombreCliente(reserva.getUsuarioCliente().getNombre());
						
						slot.setTelefonoCliente(reserva.getUsuarioCliente().getTel());
					}
				}
					
				if ("LIBRE".equals(slot.getTipo())) {

						boolean tieneExcepcion = excepciones.stream().anyMatch(excepcion ->

						excepcion.getFecha().equals(fecha)&&(Boolean.TRUE.equals(excepcion.getDiaCompleto())||
								(!horaActual.isBefore(excepcion.getHoraInicio())&& horaActual.isBefore(excepcion.getHoraFin()))));

						if (tieneExcepcion) {

							slot.setTipo("EXCEPCION");
						}
					
				}

	if ("LIBRE".equals(slot.getTipo()) && disponibilidad != null && disponibilidad.getDescansoInicio() != null && disponibilidad.getDescansoFin() != null) {

					boolean esDescanso =!horaActual.isBefore(disponibilidad.getDescansoInicio())&&horaActual.isBefore(disponibilidad.getDescansoFin());

					if (esDescanso) {

						slot.setTipo("DESCANSO");
					}
				}
						

				agenda.add(slot);

				hora = hora.plusMinutes(duracionSlot);
			}
		}

		return agenda;
	}

	
	
	private void actualizarReservasCompletadas(Usuario admin) {

		LocalDate hoy = LocalDate.now();

		LocalTime ahora = LocalTime.now();

		List<Reserva> reservas = reservaRepository.findByUsuarioAdministradorAndEstadoReserva(admin,
				EstadoReserva.ACTIVA);

		reservas.forEach(reserva -> {

			boolean completada =

					reserva.getFechaReserva().isBefore(hoy)

							||

							(reserva.getFechaReserva().equals(hoy) && reserva.getHoraFin().isBefore(ahora));

			if (completada) {

				reserva.setEstadoReserva(EstadoReserva.COMPLETADA);
			}
		});
	}
	
	
	
}
