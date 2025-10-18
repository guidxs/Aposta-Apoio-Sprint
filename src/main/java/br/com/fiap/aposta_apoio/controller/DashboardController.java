package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.ResumoDTO;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import br.com.fiap.aposta_apoio.repository.ProfissionalRepository;
import br.com.fiap.aposta_apoio.repository.SessaoApoioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para dashboard e métricas gerais do sistema.
 */
@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para visualização de métricas e estatísticas do sistema")
public class DashboardController {

    private final UsuarioRepository usuarioRepository;
    private final ProfissionalRepository profissionalRepository;
    private final SessaoApoioRepository sessaoApoioRepository;

    public DashboardController(UsuarioRepository usuarioRepository,
                               ProfissionalRepository profissionalRepository,
                               SessaoApoioRepository sessaoApoioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.profissionalRepository = profissionalRepository;
        this.sessaoApoioRepository = sessaoApoioRepository;
    }

    @GetMapping("/resumo")
    @Operation(
        summary = "Obter resumo do sistema",
        description = "Retorna estatísticas gerais do sistema: total de usuários cadastrados, total de profissionais e total de sessões de apoio agendadas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso",
            content = @Content(schema = @Schema(implementation = ResumoDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ResumoDTO> resumo() {
        ResumoDTO dto = new ResumoDTO(
                usuarioRepository.count(),
                profissionalRepository.count(),
                sessaoApoioRepository.count()
        );
        return ResponseEntity.ok(dto);
    }
}
