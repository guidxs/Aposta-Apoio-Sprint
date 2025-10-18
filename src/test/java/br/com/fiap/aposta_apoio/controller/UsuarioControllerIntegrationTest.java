package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.RegistroDTO;
import br.com.fiap.aposta_apoio.dto.UsuarioDTO;
import br.com.fiap.aposta_apoio.model.EnderecoVO;
import br.com.fiap.aposta_apoio.model.Usuario;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import br.com.fiap.aposta_apoio.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para UsuarioController.
 * Valida autenticação JWT e autorização nos endpoints protegidos.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        // Criar usuário para testes
        usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@test.com");
        usuario.setTelefone("11999999999");
        usuario.setCpf("12345678901");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setEndereco(new EnderecoVO("Rua Teste", "123", "Centro", "São Paulo", "SP", "01001000"));
        usuario.setLogin("joao.silva");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setRole("USER");

        usuario = usuarioRepository.save(usuario);

        // Gerar token JWT
        token = tokenService.gerarToken(usuario);
    }

    @Test
    void deveListarUsuariosComAutenticacao() throws Exception {
        mockMvc.perform(get("/usuarios")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void naoDeveListarUsuariosSemAutenticacao() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    void naoDeveListarUsuariosComTokenInvalido() throws Exception {
        mockMvc.perform(get("/usuarios")
                .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveBuscarUsuarioPorIdComAutenticacao() throws Exception {
        mockMvc.perform(get("/usuarios/" + usuario.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@test.com"));
    }

    @Test
    void deveRetornar404ParaUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/usuarios/999")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarUsuarioComAutenticacao() throws Exception {
        UsuarioDTO usuarioAtualizado = new UsuarioDTO(
            usuario.getId(),
            "João Silva Atualizado",
            "joao.novo@test.com",
            "11888888888",
            "12345678901",
            LocalDate.of(1990, 1, 1),
            new EnderecoVO("Rua Nova", "456", "Bairro Novo", "São Paulo", "SP", "02002000")
        );

        mockMvc.perform(put("/usuarios/" + usuario.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"))
                .andExpect(jsonPath("$.email").value("joao.novo@test.com"));
    }

    @Test
    void deveDeletarUsuarioSemSessoesComAutenticacao() throws Exception {
        mockMvc.perform(delete("/usuarios/" + usuario.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}

