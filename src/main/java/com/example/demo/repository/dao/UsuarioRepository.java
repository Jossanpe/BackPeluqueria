package com.example.demo.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.RolUsuario;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByTelAndTenant_Subdominio(String tel, String Subdominio);

	Optional<Usuario> findByTel(String tel);

	Optional<Usuario>findFirstByRolUsuario(RolUsuario rolUsuario);
}
