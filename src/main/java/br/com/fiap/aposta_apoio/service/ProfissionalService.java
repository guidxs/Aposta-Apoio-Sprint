package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.ProfissionalDTO;
import br.com.fiap.aposta_apoio.model.Profissional;
import br.com.fiap.aposta_apoio.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {
    private final ProfissionalRepository repository;

    public ProfissionalService(ProfissionalRepository repository) {
        this.repository = repository;
    }

    public ProfissionalDTO criar(ProfissionalDTO dto) {
        Profissional profissional = new Profissional();
        profissional.setNome(dto.nome());
        profissional.setEmail(dto.email());
        profissional.setEspecialidade(dto.especialidade());
        profissional.setEndereco(dto.endereco());
        Profissional salvo = repository.save(profissional);
        return toDTO(salvo);
    }

    public List<ProfissionalDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<ProfissionalDTO> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }

    public ProfissionalDTO buscarPorId(Long id) {
        Profissional profissional = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        return toDTO(profissional);
    }

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

    public void remover(Long id) {
        Profissional profissional = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        repository.delete(profissional);
    }

    private ProfissionalDTO toDTO(Profissional p) {
        return new ProfissionalDTO(p.getId(), p.getNome(), p.getEmail(), p.getEspecialidade(), p.getEndereco());
    }
}
