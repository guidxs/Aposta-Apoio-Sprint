package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.UsuarioDTO;
import br.com.fiap.aposta_apoio.model.Usuario;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import br.com.fiap.aposta_apoio.repository.SessaoApoioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final SessaoApoioRepository sessaoRepository;

    public UsuarioService(UsuarioRepository repository, SessaoApoioRepository sessaoRepository) {
        this.repository = repository;
        this.sessaoRepository = sessaoRepository;
    }

    public UsuarioDTO criar(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        usuario.setCpf(dto.cpf());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setEndereco(dto.endereco());
        Usuario salvo = repository.save(usuario);
        return toDTO(salvo);
    }

    public List<UsuarioDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<UsuarioDTO> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return toDTO(usuario);
    }

    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        usuario.setCpf(dto.cpf());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setEndereco(dto.endereco());
        Usuario atualizado = repository.save(usuario);
        return toDTO(atualizado);
    }

    public void remover(Long id) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        long qtd = sessaoRepository.countByUsuario_Id(id);
        if (qtd > 0) {
            throw new IllegalStateException("Usuário possui sessões vinculadas e não pode ser removido.");
        }
        repository.delete(usuario);
    }

    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNome(), u.getEmail(), u.getTelefone(), u.getCpf(), u.getDataNascimento(), u.getEndereco());
    }
}
