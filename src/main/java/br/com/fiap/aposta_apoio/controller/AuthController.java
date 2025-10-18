package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.LoginDTO;
import br.com.fiap.aposta_apoio.dto.RegistroDTO;
import br.com.fiap.aposta_apoio.dto.TokenDTO;
import br.com.fiap.aposta_apoio.dto.UsuarioDTO;
import br.com.fiap.aposta_apoio.model.Usuario;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import br.com.fiap.aposta_apoio.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pela autenticação e registro de usuários.
 * Expõe endpoints públicos para login e cadastro.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints públicos de autenticação, login e registro de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                         TokenService tokenService,
                         UsuarioRepository usuarioRepository,
                         PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint de login que retorna um token JWT.
     */
    @PostMapping("/login")
    @Operation(
        summary = "Fazer login",
        description = "Autentica um usuário com login e senha, retornando um token JWT válido por 1 hora. Use este token no header Authorization: Bearer {token} para acessar endpoints protegidos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = TokenDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "400", description = "Dados de login inválidos")
    })
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.senha());

        Authentication authentication = authenticationManager.authenticate(authToken);
        Usuario usuario = (Usuario) authentication.getPrincipal();

        String token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new TokenDTO(
            token,
            "Bearer",
            tokenService.getExpiration()
        ));
    }

    /**
     * Endpoint de registro que cria um novo usuário com credenciais.
     */
    @PostMapping("/registro")
    @Operation(
        summary = "Registrar novo usuário",
        description = "Cria um novo usuário no sistema com login, senha (criptografada com BCrypt) e informações pessoais. O campo 'role' é opcional (padrão: USER)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Login já existe ou dados inválidos"),
        @ApiResponse(responseCode = "400", description = "Dados de registro inválidos")
    })
    public ResponseEntity<UsuarioDTO> registro(@RequestBody @Valid RegistroDTO registroDTO) {
        // Verificar se o login já existe
        if (usuarioRepository.existsByLogin(registroDTO.login())) {
            return ResponseEntity.badRequest().build();
        }

        // Criar novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(registroDTO.nome());
        usuario.setEmail(registroDTO.email());
        usuario.setTelefone(registroDTO.telefone());
        usuario.setCpf(registroDTO.cpf());
        usuario.setDataNascimento(registroDTO.dataNascimento());
        usuario.setEndereco(registroDTO.endereco());
        usuario.setLogin(registroDTO.login());
        usuario.setSenha(passwordEncoder.encode(registroDTO.senha()));
        usuario.setRole(registroDTO.role() != null ? registroDTO.role() : "USER");

        Usuario salvo = usuarioRepository.save(usuario);

        UsuarioDTO dto = new UsuarioDTO(
            salvo.getId(),
            salvo.getNome(),
            salvo.getEmail(),
            salvo.getTelefone(),
            salvo.getCpf(),
            salvo.getDataNascimento(),
            salvo.getEndereco()
        );

        return ResponseEntity.status(201).body(dto);
    }
}
