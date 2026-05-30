package com.example.demo.repository.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tenant")
    private Long id;

    @Column(name = "nombre_negocio", nullable = false, unique = true)
    private String nombreNegocio;

    @Column(nullable = false, unique = true)
    private String subdominio;

    private Boolean activo;

    private String logo;

    private String foto;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    
    // RELACION CON USUARIO
    
    @OneToMany(mappedBy = "tenant")
    private List<Usuario> usuarios;

}