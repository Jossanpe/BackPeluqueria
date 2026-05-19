package com.example.demo.service;



import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.UsuarioDTO;


public interface UsuarioService {
	void save(UsuarioDTO usuarioDTO, MultipartFile fotoperfil);

}
