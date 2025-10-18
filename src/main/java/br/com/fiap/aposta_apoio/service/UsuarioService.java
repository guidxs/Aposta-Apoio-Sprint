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

/**
 * Implementação do serviço de usuários.
 * Aplicação dos princípios SOLID:
 * - SRP (Single Responsibility): responsável apenas pela lógica de negócio de usuários
 * - OCP (Open/Closed): aberto para extensão via interface, fechado para modificação
 * - LSP (Liskov Substitution): pode ser substituído por qualquer implementação de IUsuarioService
 * - ISP (Interface Segregation): interface específica para operações de usuário
 * - DIP (Dependency Inversion): depende de abstrações (UsuarioRepository, SessaoApoioRepository)
 */
@Service
public class UsuarioService implements IUsuarioService {
    private final UsuarioRepository repository;
    private final SessaoApoioRepository sessaoRepository;

    public UsuarioService(UsuarioRepository repository, SessaoApoioRepository sessaoRepository) {
        this.repository = repository;
        this.sessaoRepository = sessaoRepository;
    }

    @Override
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

    @Override
    public List<UsuarioDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UsuarioDTO> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return toDTO(usuario);
    }

    @Override
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

    @Override
    public void deletar(Long id) {
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
