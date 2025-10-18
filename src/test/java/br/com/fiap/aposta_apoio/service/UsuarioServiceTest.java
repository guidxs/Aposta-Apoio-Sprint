package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.UsuarioDTO;
import br.com.fiap.aposta_apoio.model.EnderecoVO;
import br.com.fiap.aposta_apoio.model.Usuario;
import br.com.fiap.aposta_apoio.repository.SessaoApoioRepository;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o UsuarioService.
 * Utiliza Mockito para isolar a lógica de negócio das dependências.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SessaoApoioRepository sessaoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private EnderecoVO endereco;

    @BeforeEach
    void setUp() {
        endereco = new EnderecoVO("Rua Teste", "123", "Centro", "São Paulo", "SP", "01001000");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@test.com");
        usuario.setTelefone("11999999999");
        usuario.setCpf("12345678901");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setEndereco(endereco);

        usuarioDTO = new UsuarioDTO(
            null,
            "João Silva",
            "joao@test.com",
            "11999999999",
            "12345678901",
            LocalDate.of(1990, 1, 1),
            endereco
        );
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        UsuarioDTO resultado = usuarioService.criar(usuarioDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.nome());
        assertEquals("joao@test.com", resultado.email());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveListarTodosUsuarios() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> resultado = usuarioService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).nome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarUsuarioPorId() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO resultado = usuarioService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("João Silva", resultado.nome());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.buscarPorId(999L);
        });
        verify(usuarioRepository, times(1)).findById(999L);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        UsuarioDTO resultado = usuarioService.atualizar(1L, usuarioDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.nome());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}
