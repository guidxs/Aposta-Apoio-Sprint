package br.com.fiap.aposta_apoio.repository;

import br.com.fiap.aposta_apoio.model.SessaoApoio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoApoioRepository extends JpaRepository<SessaoApoio, Long> {
    long countByUsuario_Id(Long usuarioId);
}
