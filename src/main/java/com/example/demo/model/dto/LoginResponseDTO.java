package com.example.demo.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO implements Serializable  {

	private static final long serialVersionUID=1L;
	

		private String tel;
		private String nombre;
		private String rol;
		private String token;
	}


