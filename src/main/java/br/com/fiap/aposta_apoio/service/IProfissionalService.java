package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.ProfissionalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface que define o contrato para serviços de profissional.
 * Aplicação do princípio SOLID: Interface Segregation Principle (ISP)
 * e Dependency Inversion Principle (DIP).
 */
public interface IProfissionalService {
    ProfissionalDTO criar(ProfissionalDTO dto);
    List<ProfissionalDTO> listar();
    Page<ProfissionalDTO> listarPaginado(Pageable pageable);
    ProfissionalDTO buscarPorId(Long id);
    ProfissionalDTO atualizar(Long id, ProfissionalDTO dto);
    void deletar(Long id);
}

