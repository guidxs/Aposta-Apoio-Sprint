package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.SessaoApoioDTO;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para o SessaoApoioController.
 * Valida o fluxo completo da API, incluindo regras de negócio e persistência.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SessaoApoioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveListarSessoes() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/sessoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveRetornar404AoBuscarSessaoInexistente() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/sessoes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveRetornar400ComDadosInvalidos() throws Exception {
        // Arrange - DTO sem campos obrigatórios
        String jsonInvalido = "{\"descricao\":\"Teste\"}";

        // Act & Assert
        mockMvc.perform(post("/sessoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveRetornar400ComUsuarioOuProfissionalInexistente() throws Exception {
        // Arrange - IDs inválidos
        SessaoApoioDTO dto = new SessaoApoioDTO(
                null,
                999L,  // Usuário inexistente
                999L,  // Profissional inexistente
                LocalDateTime.now(),
                "Teste"
        );

        // Act & Assert
        mockMvc.perform(post("/sessoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar401SemAutenticacao() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/sessoes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void deveValidarPaginacao() throws Exception {
        // Act & Assert - Testa paginação
        mockMvc.perform(get("/sessoes?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable").exists())
                .andExpect(jsonPath("$.totalElements").exists());
    }
}

