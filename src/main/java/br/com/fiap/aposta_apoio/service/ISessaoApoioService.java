package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.SessaoApoioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface que define o contrato para serviços de sessão de apoio.
 * Aplicação do princípio SOLID: Interface Segregation Principle (ISP)
 * e Dependency Inversion Principle (DIP).
 */
public interface ISessaoApoioService {
    SessaoApoioDTO criar(SessaoApoioDTO dto);
    List<SessaoApoioDTO> listar();
    Page<SessaoApoioDTO> listarPaginado(Pageable pageable);
    SessaoApoioDTO buscarPorId(Long id);
    SessaoApoioDTO atualizar(Long id, SessaoApoioDTO dto);
    void deletar(Long id);
}

