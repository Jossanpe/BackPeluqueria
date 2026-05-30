package com.example.demo.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO implements Serializable {
	private static final long serialVersionUID=1L;
	

	private String tel;
	private String claveSeguridad;
	private String tenant;
}
