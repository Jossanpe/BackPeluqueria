package com.example.demo.web.webservices;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.LoginResponseDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.model.dto.UsuarioRegistroDTO;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/usuarios")
public class UsuarioRestController {
	@Autowired
	private UsuarioService usuarioService;
	
	
	@PostMapping(value= "/add", consumes="multipart/form-data")
	public ResponseEntity<UsuarioRegistroDTO> add(@ModelAttribute UsuarioRegistroDTO usuarioRegistroDTO, @RequestParam(value="fotoperfil", required=false) MultipartFile fotoperfil, HttpServletRequest request){
		
		//GUARDAR USUARIO
		usuarioService.saveRegistro(usuarioRegistroDTO, fotoperfil, usuarioRegistroDTO.getTenant());
		
		return new ResponseEntity<>(usuarioRegistroDTO, HttpStatus.OK);
		
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO>login(@RequestBody LoginDTO loginDTO) {

	    LoginResponseDTO response = usuarioService.login(loginDTO);

	    return ResponseEntity.ok(response);

	}
	
	

	
	
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@GetMapping("/clientes")
	public List<UsuarioDTO> obtenerClientes(){
		 return usuarioService.obtenerClientesAdministrador();
	}
	
	
	@GetMapping("/perfil")
	public ResponseEntity<UsuarioDTO> obtenerPerfil(Authentication authentication) {

		String tel = authentication.getName();

		UsuarioDTO usuario = usuarioService.obtenerPerfil(tel);

	    System.out.println("USUARIO: " + usuario);
		return ResponseEntity.ok(usuario);
	}
	
	@PutMapping(value = "/update", consumes = "multipart/form-data")
	public ResponseEntity<Void> actualizarPerfil(Authentication authentication,

			@ModelAttribute UsuarioDTO usuarioDTO,

			@RequestParam(value = "fotoperfil", required = false) MultipartFile fotoperfil) {

		String tel = authentication.getName();

		usuarioService.actualizarPerfil(tel, usuarioDTO, fotoperfil);

		return ResponseEntity.ok().build();
	}
	
	
	
	
	
	
	

}
