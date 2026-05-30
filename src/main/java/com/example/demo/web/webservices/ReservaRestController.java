package com.example.demo.web.webservices;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ReservaAdminDTO;
import com.example.demo.model.dto.ReservaConsultaClienteDTO;
import com.example.demo.model.dto.ReservaDTO;
import com.example.demo.model.dto.SlotsDiaDTO;
import com.example.demo.service.ReservaService;

import lombok.RequiredArgsConstructor;

@RestController

@RequiredArgsConstructor

public class ReservaRestController {

	private final ReservaService reservaService;

	@GetMapping("/reservas/slots")
	public List<SlotsDiaDTO> obtenerSlotsSemana(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicioSemana) {

		return reservaService.obtenerSlotsSemana(fechaInicioSemana);
	}
	
	
	

	@PostMapping("/reservas/add")
	public void crearReserva(@RequestBody ReservaDTO dto) {
		reservaService.crearReserva(dto);
	}

	@PostMapping("/reservas/admin/add")
	public void crearReservaAdmin(@RequestBody ReservaAdminDTO dto) {

		reservaService.crearReservaAdmin(dto);
	}
	
	
	@GetMapping("/reservas/cliente")
	public ReservaConsultaClienteDTO obtenerReservaActivaCliente() {

		return reservaService.obtenerReservaCliente();
	}	
	
	@PutMapping("reservas/cancelar")
	public void cancelarReserva() {
		reservaService.cancelarReserva();
	}

}