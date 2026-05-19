package com.example.demo.web.webservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins="http://localhost:4200", allowCredentials="true")
public class UsuarioRestController {
	@Autowired
	private UsuarioService usuarioService;
	
	
	@PostMapping(value= "/add", consumes="multipart/form-data")
	public ResponseEntity<UsuarioDTO> add(@ModelAttribute UsuarioDTO usuarioDTO, @RequestParam(value="fotoperfil", required=false) MultipartFile fotoperfil){
		
		usuarioService.save(usuarioDTO, fotoperfil);
		if(usuarioDTO==null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<>(usuarioDTO,HttpStatus.OK);
		}
	}
	

}
