package com.example.demo.repository.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.repository.entity.Disponibilidad;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.DiaSemana;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

	// OBTENER DISPONIBILIDADES ACTIVAS

	List<Disponibilidad> findByUsuarioAdministradorAndActivaTrue(Usuario usuarioAdministrador);

	List<Disponibilidad> findByUsuarioAdministradorAndActivaTrueOrderByDiaSemanaAsc(Usuario usuarioAdministrador);

	Optional<Disponibilidad>findByDiaSemanaAndActivaTrue(DiaSemana diaSemana);

}