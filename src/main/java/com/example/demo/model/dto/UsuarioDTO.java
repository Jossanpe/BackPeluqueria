
	package com.example.demo.model.dto;

	import java.io.Serializable;
import java.util.Date;

import com.example.demo.repository.entity.Usuario;

import lombok.Data;

	@Data
	public class UsuarioDTO implements Serializable {
		
		//propiedad que identifica la versión de cada objeto transportado
			private static final long serialVersionUID=1L;
			
			private Long id;
			private String email;
			private String claveSeguridad;
			private String nombre;			
			private String tel;
			private String sexo;
			private String cp;
			private String direccion;
			
			
			private Date fechanacimiento;
			
			private String rutafoto;
		

			
			
			
			public static Usuario convertToEntity (UsuarioDTO u2) {
				
				Usuario u1= new Usuario();
				u1.setId(u2.getId());
				u1.setEmail(u2.getEmail());
				u1.setClaveSeguridad(u2.getClaveSeguridad());
				u1.setNombre(u2.getNombre());
				u1.setTel(u2.getTel());
				u1.setSexo(u2.getSexo());
				u1.setCp(u2.getCp());
				u1.setDireccion(u2.getDireccion());
				u1.setFechanacimiento(u2.getFechanacimiento());
				u1.setRutafoto(u2.getRutafoto());
				
				return u1;
			}
			

			public static UsuarioDTO convertToDTO (Usuario u2) {
				
				UsuarioDTO u1= new UsuarioDTO();
				u1.setId(u2.getId());
				u1.setEmail(u2.getEmail());
				u1.setClaveSeguridad(u2.getClaveSeguridad());
				u1.setNombre(u2.getNombre());
				u1.setTel(u2.getTel());
				u1.setSexo(u2.getSexo());
				u1.setCp(u2.getCp());
				u1.setDireccion(u2.getDireccion());
				u1.setFechanacimiento(u2.getFechanacimiento());
				u1.setRutafoto(u2.getRutafoto());
				
				return u1;
			}
	}



