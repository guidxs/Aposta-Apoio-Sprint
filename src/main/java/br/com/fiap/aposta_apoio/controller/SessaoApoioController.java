package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.SessaoApoioDTO;
import br.com.fiap.aposta_apoio.service.ISessaoApoioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Controller para operações com sessões de apoio.
 * Gerencia agendamentos entre usuários e profissionais.
 */
@RestController
@RequestMapping("/sessoes")
@Tag(name = "Sessões de Apoio", description = "Endpoints para gerenciamento de sessões de apoio entre usuários e profissionais")
public class SessaoApoioController {
    private final ISessaoApoioService service;

    public SessaoApoioController(ISessaoApoioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
        summary = "Criar nova sessão de apoio",
        description = "Agenda uma nova sessão de apoio entre um usuário e um profissional. Requer IDs válidos de usuário e profissional."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso",
            content = @Content(schema = @Schema(implementation = SessaoApoioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário/profissional não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<SessaoApoioDTO> criar(@RequestBody @Valid SessaoApoioDTO dto) {
        SessaoApoioDTO criado = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/sessoes/" + criado.id()))
                .body(criado);
    }

    @GetMapping
    @Operation(
        summary = "Listar todas as sessões",
        description = "Retorna uma lista paginada de todas as sessões de apoio agendadas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sessões retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Page<SessaoApoioDTO>> listar(
        @Parameter(description = "Parâmetros de paginação (page, size, sort)") Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar sessão por ID",
        description = "Retorna os dados de uma sessão específica através do seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sessão encontrada",
            content = @Content(schema = @Schema(implementation = SessaoApoioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<SessaoApoioDTO> buscarPorId(
        @Parameter(description = "ID da sessão", required = true, example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar sessão",
        description = "Atualiza os dados de uma sessão existente, incluindo data/hora, descrição e participantes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sessão atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = SessaoApoioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<SessaoApoioDTO> atualizar(
        @Parameter(description = "ID da sessão", required = true, example = "1") @PathVariable Long id,
        @RequestBody @Valid SessaoApoioDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar sessão",
        description = "Cancela e remove uma sessão de apoio do sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sessão deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> remover(
        @Parameter(description = "ID da sessão", required = true, example = "1") @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
