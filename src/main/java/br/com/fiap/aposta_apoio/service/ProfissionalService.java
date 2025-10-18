package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.ProfissionalDTO;
import br.com.fiap.aposta_apoio.model.Profissional;
import br.com.fiap.aposta_apoio.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de profissionais.
 * Aplicação dos princípios SOLID:
 * - SRP: responsável apenas pela lógica de negócio de profissionais
 * - OCP: aberto para extensão via interface
 * - DIP: depende de abstrações (ProfissionalRepository)
 */
@Service
public class ProfissionalService implements IProfissionalService {
    private final ProfissionalRepository repository;

    public ProfissionalService(ProfissionalRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProfissionalDTO criar(ProfissionalDTO dto) {
        Profissional profissional = new Profissional();
        profissional.setNome(dto.nome());
        profissional.setEmail(dto.email());
        profissional.setEspecialidade(dto.especialidade());
        profissional.setEndereco(dto.endereco());
        Profissional salvo = repository.save(profissional);
        return toDTO(salvo);
    }

    @Override
    public List<ProfissionalDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProfissionalDTO> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public ProfissionalDTO buscarPorId(Long id) {
        Profissional profissional = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        return toDTO(profissional);
    }

    @Override
    public ProfissionalDTO atualizar(Long id, ProfissionalDTO dto) {
        Profissional profissional = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        profissional.setNome(dto.nome());
        profissional.setEmail(dto.email());
        profissional.setEspecialidade(dto.especialidade());
        profissional.setEndereco(dto.endereco());
        Profissional atualizado = repository.save(profissional);
        return toDTO(atualizado);
    }

    @Override
    public void deletar(Long id) {
        Profissional profissional = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        repository.delete(profissional);
    }

    private ProfissionalDTO toDTO(Profissional p) {
        return new ProfissionalDTO(p.getId(), p.getNome(), p.getEmail(), p.getEspecialidade(), p.getEndereco());
    }
}
