package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.config.SecurityUtils;
import com.example.demo.mapper.DisponibilidadMapper;
import com.example.demo.model.dto.DisponibilidadConfiguracionDTO;
import com.example.demo.repository.dao.DisponibilidadRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Disponibilidad;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.DiaSemana;
import com.example.demo.service.DisponibilidadService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/*RESPONSABILIDADES: OBTENER ADMIN AUTENTICADO, DESACTIVAR LAS DISPONIBILIDADES ANTIGUAS, 
 * ADAPTAR DISPONIBILIDAD FRONT A DISPONIBILIDAD DE REGISTRO BD CREANDO MULTIPLES ENTIDADES QUE SERÁN CADA LINEA DE LA BD,
 * GUARDARLAS Y RECONSTRUIR 2 DTO PARA FRONTED, UNO QUE DEVUELVA LA DISPONIBILIDAD PARA EL PANEL DE CONTROL, Y OTRA
 * CON LOS DATOS TRANSFORMADOS EN TURNOS PARA LA DISPONIBILIDAD DE LOS CLIENTES.
 * */
@Transactional
@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {
	
	  private final DisponibilidadRepository disponibilidadRepository;

	    private final UsuarioRepository usuarioRepository;
	    private final DisponibilidadMapper disponibilidadMapper;
	    
	    
	   
	    //CONVERSOR DE TODOS LOS DIAS QUE SE ENCUENTRAN EN EL RANGO INDICADO POR EL ADMINISTRADOR, NECESITAMOS UNA FILA POR DIA.
	    //METODO INTERNO QUE NO ES NECESARIO MOSTRARLO, POR ESTO NO SE PONE EN LA INTERFAZ
	    private List<DiaSemana> obtenerRangoDias(String inicio,String fin) {
			
			List<DiaSemana> dias = new ArrayList<>();

			DiaSemana diaInicio = DiaSemana.valueOf(inicio.toUpperCase());

			DiaSemana diaFin = DiaSemana.valueOf(fin.toUpperCase());

			DiaSemana[] valores = DiaSemana.values();

			int inicioIndex = diaInicio.ordinal();

			int finIndex = diaFin.ordinal();

			for (int i = inicioIndex; i <= finIndex; i++) {
				dias.add(valores[i]);
			}

			return dias;
		}
	
	
	
	
	
		
	//GUARDA LA DISPONIBILIDAD DEL ADMINISTRADOR EN LA BASE DE DATOS. DESACTIVAR LAS ANTIGUAS, GENERAR TODOS LOS DIAS NUEVOS, ACTIVARLOS Y GUARDARLOS
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@Override
		public DisponibilidadConfiguracionDTO guardarDisponibilidad(DisponibilidadConfiguracionDTO dto) {
	
			    // 1 OBTENER ADMIN

				String telefono = SecurityUtils.obtenerTelUsuario();

				Usuario admin = usuarioRepository.findByTel(telefono).orElseThrow();

			    // 2 DESACTIVAR ANTIGUAS

			    List<Disponibilidad> antiguas =disponibilidadRepository.findByUsuarioAdministradorAndActivaTrue(admin);
			    	for(Disponibilidad disponibilidad: antiguas){

			        disponibilidad.setActiva(false);
			        disponibilidad.setFechaInactivacion(LocalDateTime.now());
			    }
			    	
			    	disponibilidadRepository.saveAll(antiguas);

			    // 3 GENERAR NUEVAS

			    List<DiaSemana> dias = obtenerRangoDias(dto.getDiaInicio(), dto.getDiaFin());

			    List<Disponibilidad> nuevas = new ArrayList<>();

			    	for(
			    			DiaSemana dia: dias
			    ){

			    		//UTILIZAMOS EL MAPPEADOR DTO EN ENTIDAD PARA NO SATURAR ESTE METODO
			       Disponibilidad disponibilidad= disponibilidadMapper.disponibilidadConfiguracionConvertirEntidad(dto, dia, admin);

			        nuevas.add(disponibilidad);
			    }

			    disponibilidadRepository.saveAll(nuevas);
			    
			    return dto;
			}
			
		
		
		
		
		
		
		//OBTENEMOS TODAS LAS DISPONIBILIDADES ACTIVAS , RECONSTRUIMOS EL RANGO Y DEVOLVEMOS EL DTO
	@PreAuthorize("hasRole('ADMINISTRADOR')")	
	@Override
		public DisponibilidadConfiguracionDTO obtenerDisponibilidad() {
		String telefono = SecurityUtils.obtenerTelUsuario();

		Usuario admin = usuarioRepository.findByTel(telefono).orElseThrow();

			

			List<Disponibilidad> disponibilidades = disponibilidadRepository.findByUsuarioAdministradorAndActivaTrueOrderByDiaSemanaAsc(admin);

			if (disponibilidades.isEmpty()) {
				return null;
			}

			return disponibilidadMapper.disponibilidadConfiguracionconvertirDTO(disponibilidades);
				}
		}
	

