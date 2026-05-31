package com.example.demo.model.dto;


import java.io.Serializable;

import com.example.demo.repository.entity.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nombre;

    private String tel;

    public static UsuarioDTO convertToDTO(Usuario usuario) {

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());

        dto.setNombre(usuario.getNombre());

        dto.setTel(usuario.getTel());

        return dto;
    }
}