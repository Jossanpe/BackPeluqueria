package com.example.demo.repository.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.Tenant;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.repository.entity.enums.RolUsuario;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByTelAndTenant_Subdominio(String tel, String Subdominio);

	Optional<Usuario> findByTel(String tel);

	Optional<Usuario> findByTelAndTenant(String tel, Tenant tenant);

	Optional<Usuario> findByRolUsuarioAndTenant(RolUsuario rolUsuario, Tenant tenant);

	List<Usuario> findByTenantAndRolUsuario(Tenant tenant, RolUsuario rolUsuario);
}
