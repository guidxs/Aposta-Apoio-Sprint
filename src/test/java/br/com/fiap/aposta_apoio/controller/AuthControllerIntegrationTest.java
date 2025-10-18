package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.LoginDTO;
import br.com.fiap.aposta_apoio.dto.RegistroDTO;
import br.com.fiap.aposta_apoio.model.EnderecoVO;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para AuthController.
 * Testa o fluxo completo de autenticação (registro e login).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private RegistroDTO registroDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        EnderecoVO endereco = new EnderecoVO(
            "Rua Teste",
            "123",
            "Centro",
            "São Paulo",
            "SP",
            "01001000"
        );

        registroDTO = new RegistroDTO(
            "João Silva",
            "joao@test.com",
            "11999999999",
            "12345678901",
            LocalDate.of(1990, 1, 1),
            endereco,
            "joao.silva",
            "senha123",
            "USER"
        );

        loginDTO = new LoginDTO("joao.silva", "senha123");
    }

    @Test
    void deveRegistrarNovoUsuarioComSucesso() throws Exception {
        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@test.com"));
    }

    @Test
    void naoDeveRegistrarUsuarioComLoginDuplicado() throws Exception {
        // Primeiro registro
        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated());

        // Segundo registro com mesmo login
        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveFazerLoginComSucessoERetornarToken() throws Exception {
        // Registrar usuário primeiro
        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated());

        // Fazer login
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600000));
    }

    @Test
    void naoDeveFazerLoginComSenhaIncorreta() throws Exception {
        // Registrar usuário
        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated());

        // Tentar login com senha incorreta
        LoginDTO loginErrado = new LoginDTO("joao.silva", "senhaERRADA");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginErrado)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void naoDeveFazerLoginComUsuarioInexistente() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void naoDeveRegistrarUsuarioComDadosInvalidos() throws Exception {
        RegistroDTO registroInvalido = new RegistroDTO(
            "", // nome vazio
            "email-invalido", // email inválido
            "",
            "",
            null,
            null,
            "",
            "",
            null
        );

        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroInvalido)))
                .andExpect(status().isBadRequest());
    }
}

