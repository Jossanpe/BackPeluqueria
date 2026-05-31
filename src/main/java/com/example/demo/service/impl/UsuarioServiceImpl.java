package com.example.demo.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.JwtService;
import com.example.demo.config.SecurityUtils;
import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.LoginResponseDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.model.dto.UsuarioRegistroDTO;
import com.example.demo.repository.dao.TenantRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Tenant;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.RolUsuario;
import com.example.demo.service.UsuarioService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired 
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TenantRepository tenantRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	
	@Value("${carpeta.fotos.subidas}")
	private String rutaFotos;
	
	//inyectamos un valor desde el archivo applicaton.properties hacia una variable de esta clase. I
	//Indicamos que busque la propieda llamada "app.upload.dir" de application.properties y que la use, si no que utilice el valor por defecto "uploads".
	//La carpeta uploads se encuentra a nivel de SRC, dentro del proyecto.
	//Definimos el valor de la ruta en application-properties para adaptarla a produccion.
	//@Value("${carpetas.fotos.subidas:uploads}")
	
	

	@Override
	public void saveRegistro(UsuarioRegistroDTO usuarioRegistroDTO, MultipartFile fotoperfil, String subdominio) {
		//BUSCAR TENANT
		Tenant tenant = tenantRepository.findBySubdominio(subdominio).orElseThrow();
		
		//DTO -> ENTITY
		Usuario usuario = UsuarioRegistroDTO.convertToEntity(usuarioRegistroDTO);
		
		//ASOCIAR TENANT
		usuario.setTenant(tenant);
		
		//ROL POR DEFECTO
		
		usuario.setRolUsuario(RolUsuario.CLIENTE);
		
		//DESHABILITADO
		usuario.setHabilitado(false);
		usuario.setClaveSeguridad(passwordEncoder.encode(usuario.getClaveSeguridad()));
		
		//FOTO PERFIL
		if(fotoperfil != null && !fotoperfil.isEmpty()) {
		try {
			String nombreFoto = System.currentTimeMillis()+ "_" + fotoperfil.getOriginalFilename();
			Path rutaArchivo = Paths.get(rutaFotos + nombreFoto);
			Files.write(rutaArchivo,fotoperfil.getBytes());
		
			usuario.setRutafoto(nombreFoto);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
		
		//Guardamos el usuario
		usuarioRepository.save(usuario);
		
	}

	@Override
	public LoginResponseDTO login(LoginDTO loginDTO) {
		
		  Usuario usuario = usuarioRepository.findByTelAndTenant_Subdominio(loginDTO.getTel(),loginDTO.getTenant()).orElseThrow();

		  boolean passwordCorrecta = passwordEncoder.matches(loginDTO.getClaveSeguridad(),usuario.getClaveSeguridad());
		  
		  if(!passwordCorrecta) { throw new RuntimeException("Password incorrecta");}
		  
		  String token = jwtService.generarToken(usuario.getTel(), usuario.getRolUsuario().name(),usuario.getTenant().getSubdominio());

		    LoginResponseDTO responseDTO = new LoginResponseDTO();
		    
		    responseDTO.setToken(token);
		    
		    responseDTO.setNombre(usuario.getNombre());

		    responseDTO.setTel(usuario.getTel());

		    responseDTO.setRol(usuario.getRolUsuario().name());

		    return responseDTO;
		
	}
	
	
	
	@Override
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public List<UsuarioDTO> obtenerClientesAdministrador() {

		Usuario admin = usuarioRepository.findByTel(SecurityUtils.obtenerTelUsuario()).orElseThrow();

		return usuarioRepository.findByTenantAndRolUsuario(admin.getTenant(), RolUsuario.CLIENTE).stream()
				.map(UsuarioDTO::convertToDTO).toList();
	}
	
	
	
	
}
