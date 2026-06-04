package com.example.demo.model.dto;


import java.io.Serializable;
import java.time.LocalDate;

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
    private String direccion;
    private String cp;
    private LocalDate fechanacimiento;
    private String email;
    private String Rutafoto;

    public static UsuarioDTO convertToDTO(Usuario usuario) {

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());

        dto.setNombre(usuario.getNombre());

        dto.setTel(usuario.getTel());
        dto.setDireccion(usuario.getDireccion());
        dto.setCp(usuario.getCp());
        dto.setFechanacimiento(usuario.getFechanacimiento());
        dto.setEmail(usuario.getEmail());
        dto.setRutafoto(usuario.getRutafoto());

        return dto;
    }
}