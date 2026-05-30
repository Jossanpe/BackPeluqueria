package com.example.demo.config;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

	// OBTENER TELEFONO USUARIO LOGUEADO
	public static String obtenerTelUsuario() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return auth.getName();
	}
}