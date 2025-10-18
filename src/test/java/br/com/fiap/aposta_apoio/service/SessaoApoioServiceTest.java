package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.SessaoApoioDTO;
import br.com.fiap.aposta_apoio.model.Profissional;
import br.com.fiap.aposta_apoio.model.SessaoApoio;
import br.com.fiap.aposta_apoio.model.Usuario;
import br.com.fiap.aposta_apoio.repository.ProfissionalRepository;
import br.com.fiap.aposta_apoio.repository.SessaoApoioRepository;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o SessaoApoioService.
 * Demonstra uso de múltiplos mocks e validação de regras de negócio.
 */
@ExtendWith(MockitoExtension.class)
class SessaoApoioServiceTest {

    @Mock
    private SessaoApoioRepository sessaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private SessaoApoioService sessaoService;

    private Usuario usuario;
    private Profissional profissional;
    private SessaoApoio sessao;
    private SessaoApoioDTO sessaoDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Dr. Carlos");

        sessao = new SessaoApoio();
        sessao.setId(1L);
        sessao.setUsuario(usuario);
        sessao.setProfissional(profissional);
        sessao.setDataHora(LocalDateTime.of(2025, 10, 20, 14, 0));
        sessao.setDescricao("Primeira sessão");

        sessaoDTO = new SessaoApoioDTO(
                1L,
                1L,
                1L,
                LocalDateTime.of(2025, 10, 20, 14, 0),
                "Primeira sessão"
        );
    }

    @Test
    void deveCriarSessaoComSucesso() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(sessaoRepository.save(any(SessaoApoio.class))).thenReturn(sessao);

        // Act
        SessaoApoioDTO resultado = sessaoService.criar(sessaoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.usuarioId());
        assertEquals(1L, resultado.profissionalId());
        assertEquals("Primeira sessão", resultado.descricao());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).findById(1L);
        verify(sessaoRepository, times(1)).save(any(SessaoApoio.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        SessaoApoioDTO dtoInvalido = new SessaoApoioDTO(
                null, 999L, 1L, LocalDateTime.now(), "Teste"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessaoService.criar(dtoInvalido);
        });
        verify(usuarioRepository, times(1)).findById(999L);
        verify(sessaoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoProfissionalNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findById(999L)).thenReturn(Optional.empty());

        SessaoApoioDTO dtoInvalido = new SessaoApoioDTO(
                null, 1L, 999L, LocalDateTime.now(), "Teste"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessaoService.criar(dtoInvalido);
        });
        verify(profissionalRepository, times(1)).findById(999L);
        verify(sessaoRepository, never()).save(any());
    }

    @Test
    void deveListarTodasSessoes() {
        // Arrange
        SessaoApoio sessao2 = new SessaoApoio();
        sessao2.setId(2L);
        sessao2.setUsuario(usuario);
        sessao2.setProfissional(profissional);
        sessao2.setDataHora(LocalDateTime.of(2025, 10, 25, 15, 0));
        sessao2.setDescricao("Segunda sessão");

        when(sessaoRepository.findAll()).thenReturn(Arrays.asList(sessao, sessao2));

        // Act
        List<SessaoApoioDTO> resultado = sessaoService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Primeira sessão", resultado.get(0).descricao());
        assertEquals("Segunda sessão", resultado.get(1).descricao());
        verify(sessaoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarSessaoPorId() {
        // Arrange
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));

        // Act
        SessaoApoioDTO resultado = sessaoService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(1L, resultado.usuarioId());
        assertEquals(1L, resultado.profissionalId());
        verify(sessaoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoEncontrada() {
        // Arrange
        when(sessaoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessaoService.buscarPorId(999L);
        });
        verify(sessaoRepository, times(1)).findById(999L);
    }

    @Test
    void deveAtualizarSessaoComSucesso() {
        // Arrange
        SessaoApoioDTO dtoAtualizado = new SessaoApoioDTO(
                1L,
                1L,
                1L,
                LocalDateTime.of(2025, 10, 21, 16, 0),
                "Sessão reagendada"
        );

        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(sessaoRepository.save(any(SessaoApoio.class))).thenReturn(sessao);

        // Act
        SessaoApoioDTO resultado = sessaoService.atualizar(1L, dtoAtualizado);

        // Assert
        assertNotNull(resultado);
        verify(sessaoRepository, times(1)).findById(1L);
        verify(sessaoRepository, times(1)).save(any(SessaoApoio.class));
    }

    @Test
    void deveDeletarSessaoComSucesso() {
        // Arrange
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        doNothing().when(sessaoRepository).delete(sessao);

        // Act
        sessaoService.deletar(1L);

        // Assert
        verify(sessaoRepository, times(1)).findById(1L);
        verify(sessaoRepository, times(1)).delete(sessao);
    }

    @Test
    void deveValidarRegraDeNegocioAoCriarSessao() {
        // Arrange - Testa que ambos (usuário e profissional) devem existir
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(sessaoRepository.save(any(SessaoApoio.class))).thenReturn(sessao);

        // Act
        SessaoApoioDTO resultado = sessaoService.criar(sessaoDTO);

        // Assert - Verifica que ambos os repositórios foram consultados
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).findById(1L);
    }
}

