package com.example.demo.service;



import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.LoginResponseDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.model.dto.UsuarioRegistroDTO;


public interface UsuarioService {
	void saveRegistro(UsuarioRegistroDTO usuarioRegistroDTO, MultipartFile fotoperfil, String subdominio);
	
	LoginResponseDTO login(LoginDTO loginDTO);
	List<UsuarioDTO> obtenerClientesAdministrador();

	UsuarioDTO obtenerPerfil(String tel);

	void actualizarPerfil(String tel, UsuarioDTO usuarioDTO, MultipartFile fotoperfil);

}
