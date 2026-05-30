package com.example.demo.model.dto;

import java.io.Serializable;
import com.example.demo.repository.entity.Usuario;

import lombok.Getter;
import lombok.Setter;


	@Getter
	@Setter
	public class UsuarioRegistroDTO implements Serializable {
	
		//propiedad que identifica la versión de cada objeto transportado
			private static final long serialVersionUID=1L;
			
			private String email;
			private String claveSeguridad;
			private String nombre;			
			private String tel;		
			private String tenant;
	

	
			
			
			public static Usuario convertToEntity (UsuarioRegistroDTO u2) {
				
				Usuario u1= new Usuario();
				u1.setEmail(u2.getEmail());
				u1.setClaveSeguridad(u2.getClaveSeguridad());
				u1.setNombre(u2.getNombre());
				u1.setTel(u2.getTel());			
				
				return u1;
			}
			

	}




