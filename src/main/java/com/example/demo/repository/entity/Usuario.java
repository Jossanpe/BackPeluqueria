package com.example.demo.repository.entity;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name="usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="email")
	private String email;
	
	@Column(name="clave")
	private String claveSeguridad;
	
	private String nombre;
	
	@Column(name="telefono")
	private String tel;
	
	private String sexo;
	private String cp;
	private String direccion;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechanacimiento;
	
	//private String rol;
	


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
