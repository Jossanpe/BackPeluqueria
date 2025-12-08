package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired 
	private UsuarioRepository usuarioRepository;

	@Override
	public void save(UsuarioDTO usuarioDTO) {
		//String rol="usuario";
		Usuario usuario = UsuarioDTO.convertToEntity(usuarioDTO);
		//usuario.setRol(rol);
		usuarioRepository.save(usuario);
		
	}
}
