package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.model.dto.ReservaAdminDTO;
import com.example.demo.model.dto.ReservaConsultaClienteDTO;
import com.example.demo.model.dto.ReservaDTO;
import com.example.demo.model.dto.SlotsDiaDTO;

public interface ReservaService {

	List<SlotsDiaDTO> obtenerSlotsSemana(LocalDate fechaInicioSemana);
	
	void crearReserva(ReservaDTO dto);
	void crearReservaAdmin(ReservaAdminDTO dto);
	ReservaConsultaClienteDTO obtenerReservaCliente();
	
	void cancelarReserva();
	
}