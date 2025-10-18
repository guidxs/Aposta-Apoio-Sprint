package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.ProfissionalDTO;
import br.com.fiap.aposta_apoio.model.EnderecoVO;
import br.com.fiap.aposta_apoio.model.Especialidade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para o ProfissionalController.
 * Testa a API completa, incluindo controllers, services, repositories e banco de dados.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProfissionalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveCriarProfissionalComSucesso() throws Exception {
        // Arrange
        EnderecoVO endereco = new EnderecoVO("Av. Paulista", "1000", "Bela Vista", "São Paulo", "SP", "01310100");
        ProfissionalDTO dto = new ProfissionalDTO(null, "Dr. Carlos Mendes", "carlos@test.com", Especialidade.PSICOLOGIA, endereco);

        // Act & Assert
        mockMvc.perform(post("/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. Carlos Mendes"))
                .andExpect(jsonPath("$.email").value("carlos@test.com"))
                .andExpect(jsonPath("$.especialidade").value("PSICOLOGIA"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveListarProfissionais() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveRetornar404AoBuscarProfissionalInexistente() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/profissionais/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveRetornar400ComEspecialidadeInvalida() throws Exception {
        // Arrange
        String jsonInvalido = "{\"nome\":\"Dr. Teste\",\"email\":\"teste@test.com\",\"especialidade\":\"INVALIDA\"}";

        // Act & Assert
        mockMvc.perform(post("/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar401SemAutenticacao() throws Exception {
        // Arrange
        EnderecoVO endereco = new EnderecoVO("Rua Teste", "123", "Centro", "São Paulo", "SP", "01001000");
        ProfissionalDTO dto = new ProfissionalDTO(null, "Dr. Teste", "teste@test.com", Especialidade.ORIENTACAO, endereco);

        // Act & Assert
        mockMvc.perform(post("/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}

