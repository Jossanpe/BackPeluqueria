
	package com.example.demo.model.dto;

	import java.io.Serializable;
import java.time.LocalDate;

import com.example.demo.repository.entity.Usuario;

import lombok.Getter;
import lombok.Setter;


	@Getter
	@Setter
	public class UsuarioEdicionDTO implements Serializable {
		
		//propiedad que identifica la versión de cada objeto transportado
			private static final long serialVersionUID=1L;
			
			private Long id;
			private String email;
			private String claveSeguridad;
			private String nombre;			
			private String tel;		
			private String cp;
			private String direccion;				
			private LocalDate fechanacimiento;		
			private String rutafoto;
		

			
			
			
			public static Usuario convertToEntity (UsuarioEdicionDTO u2) {
				
				Usuario u1= new Usuario();
				u1.setId(u2.getId());
				u1.setEmail(u2.getEmail());
				u1.setClaveSeguridad(u2.getClaveSeguridad());
				u1.setNombre(u2.getNombre());
				u1.setTel(u2.getTel());			
				u1.setCp(u2.getCp());
				u1.setDireccion(u2.getDireccion());
				u1.setFechanacimiento(u2.getFechanacimiento());
				u1.setRutafoto(u2.getRutafoto());
				
				return u1;
			}
			

			public static UsuarioEdicionDTO convertToDTO (Usuario u2) {
				
				UsuarioEdicionDTO u1= new UsuarioEdicionDTO();
				u1.setId(u2.getId());
				u1.setEmail(u2.getEmail());
				u1.setClaveSeguridad(u2.getClaveSeguridad());
				u1.setNombre(u2.getNombre());
				u1.setTel(u2.getTel());
				u1.setCp(u2.getCp());
				u1.setDireccion(u2.getDireccion());
				u1.setFechanacimiento(u2.getFechanacimiento());
				u1.setRutafoto(u2.getRutafoto());
				
				return u1;
			}
	}



