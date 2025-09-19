package br.com.fiap.aposta_apoio.repository;

import br.com.fiap.aposta_apoio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}

