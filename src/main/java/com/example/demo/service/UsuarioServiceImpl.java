package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired 
	private UsuarioRepository usuarioRepository;
	
	
	//inyectamos un valor desde el archivo applicaton.properties hacia una variable de esta clase. I
	//Indicamos que busque la propieda llamada "app.upload.dir" de application.properties y que la use, si no que utilice el valor por defecto "uploads".
	//La carpeta uploads se encuentra a nivel de SRC, dentro del proyecto.
	//Definimos el valor de la ruta en application-properties para adaptarla a produccion.
	//@Value("${carpetas.fotos.subidas:uploads}")
	
	

	@Override
	public void save(UsuarioDTO usuarioDTO, MultipartFile fotoperfil) {
		//String rol="usuario";
		Usuario usuario = UsuarioDTO.convertToEntity(usuarioDTO);
		//usuario.setRol(rol);
		usuarioRepository.save(usuario);
		
	}
}
