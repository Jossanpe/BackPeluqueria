package com.example.demo.web.webservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins="http://localhost:4200", allowCredentials="true")
public class UsuarioRestController {
	@Autowired
	private UsuarioService usuarioService;
	
	
	@PostMapping("/add")
	public ResponseEntity<UsuarioDTO> add(@RequestBody UsuarioDTO usuarioDTO){
		
		usuarioService.save(usuarioDTO);
		if(usuarioDTO==null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<>(usuarioDTO,HttpStatus.OK);
		}
	}

}
