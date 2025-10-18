package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.UsuarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface que define o contrato para serviços de usuário.
 * Aplicação do princípio SOLID: Interface Segregation Principle (ISP)
 * e Dependency Inversion Principle (DIP).
 */
public interface IUsuarioService {
    UsuarioDTO criar(UsuarioDTO dto);
    List<UsuarioDTO> listar();
    Page<UsuarioDTO> listarPaginado(Pageable pageable);
    UsuarioDTO buscarPorId(Long id);
    UsuarioDTO atualizar(Long id, UsuarioDTO dto);
    void deletar(Long id);
}

