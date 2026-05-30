package com.example.demo.repository.entity;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Objects;

import com.example.demo.repository.entity.enums.RolUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_tenant", nullable=false)
	private Tenant tenant;

	@Column(nullable= false)
	private String nombre;
	
	
	@Column(name="email", nullable = false, unique = true)
	private String email;
	
	@Column(name="password_hash", nullable = false)
	private String claveSeguridad;
	
	@Column(name="telefono", nullable = false, unique = true)
	private String tel;
	
	private String cp;
	private String direccion;
	
	@Column (name="ruta_foto_perfil")
	private String rutafoto;
	
	@Column(name= "fecha_nacimiento")
	private LocalDate fechanacimiento;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name= "rol_usuario", nullable = false)
	private RolUsuario rolUsuario;
	
	private Boolean habilitado;


    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;


   @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
	
	
	
	
	
	
	
	

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	
}
