package com.example.demo.repository.dao;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.repository.entity.ExcepcionDisponibilidad;

import com.example.demo.repository.entity.Usuario;

public interface ExcepcionDisponibilidadRepository extends JpaRepository<ExcepcionDisponibilidad, Long> {

	// TODAS LAS EXCEPCIONESDEL ADMIN

	List<ExcepcionDisponibilidad> findByUsuarioAdministradorOrderByFechaAsc(Usuario usuarioAdministrador);

	// EXCEPCIONES DE UNA FECHA, PARA CALCULAR DISPONIBILIDAD CLIENTE

	List<ExcepcionDisponibilidad> findByUsuarioAdministradorAndFecha(Usuario usuarioAdministrador, LocalDate fecha);

	Optional<ExcepcionDisponibilidad> findByFecha(LocalDate fecha);
}