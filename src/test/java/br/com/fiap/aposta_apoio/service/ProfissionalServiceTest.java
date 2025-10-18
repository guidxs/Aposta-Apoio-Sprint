package br.com.fiap.aposta_apoio.service;

import br.com.fiap.aposta_apoio.dto.ProfissionalDTO;
import br.com.fiap.aposta_apoio.model.EnderecoVO;
import br.com.fiap.aposta_apoio.model.Especialidade;
import br.com.fiap.aposta_apoio.model.Profissional;
import br.com.fiap.aposta_apoio.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o ProfissionalService.
 * Utiliza Mockito para isolar a lógica de negócio das dependências.
 * Demonstra uso de mocks e cobertura de código.
 */
@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Profissional profissional;
    private ProfissionalDTO profissionalDTO;
    private EnderecoVO endereco;

    @BeforeEach
    void setUp() {
        endereco = new EnderecoVO("Av. Paulista", "1000", "Bela Vista", "São Paulo", "SP", "01310100");

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Dr. Carlos Mendes");
        profissional.setEmail("carlos@test.com");
        profissional.setEspecialidade(Especialidade.PSICOLOGIA);
        profissional.setEndereco(endereco);

        profissionalDTO = new ProfissionalDTO(
                1L,
                "Dr. Carlos Mendes",
                "carlos@test.com",
                Especialidade.PSICOLOGIA,
                endereco
        );
    }

    @Test
    void deveCriarProfissionalComSucesso() {
        // Arrange
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // Act
        ProfissionalDTO resultado = profissionalService.criar(profissionalDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Dr. Carlos Mendes", resultado.nome());
        assertEquals("carlos@test.com", resultado.email());
        assertEquals(Especialidade.PSICOLOGIA, resultado.especialidade());
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }

    @Test
    void deveListarTodosProfissionais() {
        // Arrange
        Profissional profissional2 = new Profissional();
        profissional2.setId(2L);
        profissional2.setNome("Dra. Ana Santos");
        profissional2.setEmail("ana@test.com");
        profissional2.setEspecialidade(Especialidade.ORIENTACAO);
        profissional2.setEndereco(endereco);

        when(profissionalRepository.findAll()).thenReturn(Arrays.asList(profissional, profissional2));

        // Act
        List<ProfissionalDTO> resultado = profissionalService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Dr. Carlos Mendes", resultado.get(0).nome());
        assertEquals("Dra. Ana Santos", resultado.get(1).nome());
        verify(profissionalRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarProfissionalPorId() {
        // Arrange
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        // Act
        ProfissionalDTO resultado = profissionalService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Dr. Carlos Mendes", resultado.nome());
        verify(profissionalRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProfissionalNaoEncontrado() {
        // Arrange
        when(profissionalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            profissionalService.buscarPorId(999L);
        });
        verify(profissionalRepository, times(1)).findById(999L);
    }

    @Test
    void deveAtualizarProfissionalComSucesso() {
        // Arrange
        ProfissionalDTO dtoAtualizado = new ProfissionalDTO(
                1L,
                "Dr. Carlos Mendes Junior",
                "carlos.junior@test.com",
                Especialidade.PSIQUIATRIA,
                endereco
        );

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // Act
        ProfissionalDTO resultado = profissionalService.atualizar(1L, dtoAtualizado);

        // Assert
        assertNotNull(resultado);
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }

    @Test
    void deveDeletarProfissionalComSucesso() {
        // Arrange
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        doNothing().when(profissionalRepository).delete(profissional);

        // Act
        profissionalService.deletar(1L);

        // Assert
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).delete(profissional);
    }

    @Test
    void deveLancarExcecaoAoDeletarProfissionalInexistente() {
        // Arrange
        when(profissionalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            profissionalService.deletar(999L);
        });
        verify(profissionalRepository, times(1)).findById(999L);
        verify(profissionalRepository, never()).delete(any());
    }

    @Test
    void deveValidarEspecialidadeEnum() {
        // Assert - Verifica que todos os valores do enum são válidos
        assertNotNull(Especialidade.PSICOLOGIA);
        assertNotNull(Especialidade.ORIENTACAO);
        assertNotNull(Especialidade.TERAPIA_GRUPO);
        assertNotNull(Especialidade.COACHING);
        assertNotNull(Especialidade.PSIQUIATRIA);
        assertEquals(5, Especialidade.values().length);
    }
}

