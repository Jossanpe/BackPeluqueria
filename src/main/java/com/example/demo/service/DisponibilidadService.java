package com.example.demo.service;

import com.example.demo.model.dto.DisponibilidadConfiguracionDTO;

public interface DisponibilidadService {

    DisponibilidadConfiguracionDTO guardarDisponibilidad( DisponibilidadConfiguracionDTO DisponibilidadConfiguracionDTO);

    DisponibilidadConfiguracionDTO obtenerDisponibilidad();
}