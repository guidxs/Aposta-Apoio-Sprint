package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.SessaoApoioDTO;
import br.com.fiap.aposta_apoio.model.Profissional;
import br.com.fiap.aposta_apoio.model.SessaoApoio;
import br.com.fiap.aposta_apoio.model.Usuario;
import br.com.fiap.aposta_apoio.repository.ProfissionalRepository;
import br.com.fiap.aposta_apoio.repository.SessaoApoioRepository;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessaoApoioService {
    private final SessaoApoioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProfissionalRepository profissionalRepository;

    public SessaoApoioService(SessaoApoioRepository repository, UsuarioRepository usuarioRepository, ProfissionalRepository profissionalRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public SessaoApoioDTO criar(SessaoApoioDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.usuarioId());
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(dto.profissionalId());
        if (usuarioOpt.isEmpty() || profissionalOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário ou Profissional não encontrado");
        }
        SessaoApoio sessao = new SessaoApoio();
        sessao.setUsuario(usuarioOpt.get());
        sessao.setProfissional(profissionalOpt.get());
        sessao.setDataHora(dto.dataHora());
        sessao.setDescricao(dto.descricao());
        SessaoApoio salvo = repository.save(sessao);
        return new SessaoApoioDTO(salvo.getId(), salvo.getUsuario().getId(), salvo.getProfissional().getId(), salvo.getDataHora(), salvo.getDescricao());
    }

    public List<SessaoApoioDTO> listar() {
        return repository.findAll().stream()
                .map(s -> new SessaoApoioDTO(s.getId(), s.getUsuario().getId(), s.getProfissional().getId(), s.getDataHora(), s.getDescricao()))
                .collect(Collectors.toList());
    }

    public Page<SessaoApoioDTO> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable)
                .map(s -> new SessaoApoioDTO(s.getId(), s.getUsuario().getId(), s.getProfissional().getId(), s.getDataHora(), s.getDescricao()));
    }

    public SessaoApoioDTO buscarPorId(Long id) {
        SessaoApoio sessao = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sessão de apoio não encontrada"));
        return new SessaoApoioDTO(sessao.getId(), sessao.getUsuario().getId(), sessao.getProfissional().getId(), sessao.getDataHora(), sessao.getDescricao());
    }

    public SessaoApoioDTO atualizar(Long id, SessaoApoioDTO dto) {
        SessaoApoio sessao = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sessão de apoio não encontrada"));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Profissional profissional = profissionalRepository.findById(dto.profissionalId())
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        sessao.setUsuario(usuario);
        sessao.setProfissional(profissional);
        sessao.setDataHora(dto.dataHora());
        sessao.setDescricao(dto.descricao());
        SessaoApoio atualizado = repository.save(sessao);
        return new SessaoApoioDTO(atualizado.getId(), atualizado.getUsuario().getId(), atualizado.getProfissional().getId(), atualizado.getDataHora(), atualizado.getDescricao());
    }

    public void remover(Long id) {
        SessaoApoio sessao = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sessão de apoio não encontrada"));
        repository.delete(sessao);
    }
}
