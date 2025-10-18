package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.ProfissionalDTO;
import br.com.fiap.aposta_apoio.service.IProfissionalService;
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
 * Controller para operações com profissionais.
 * Aplicação de POLIMORFISMO: depende da interface IProfissionalService.
 */
@RestController
@RequestMapping("/profissionais")
@Tag(name = "Profissionais", description = "Endpoints para gerenciamento de profissionais de apoio (psicólogos, orientadores, etc)")
public class ProfissionalController {
    private final IProfissionalService service;

    public ProfissionalController(IProfissionalService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
        summary = "Criar novo profissional",
        description = "Cadastra um novo profissional de apoio no sistema. Especialidades disponíveis: PSICOLOGIA, ORIENTACAO, TERAPIA_GRUPO, COACHING, PSIQUIATRIA."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profissional criado com sucesso",
            content = @Content(schema = @Schema(implementation = ProfissionalDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou especialidade não permitida"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ProfissionalDTO> criar(@RequestBody @Valid ProfissionalDTO dto) {
        ProfissionalDTO criado = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/profissionais/" + criado.id()))
                .body(criado);
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os profissionais",
        description = "Retorna uma lista paginada de todos os profissionais cadastrados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de profissionais retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Page<ProfissionalDTO>> listar(
        @Parameter(description = "Parâmetros de paginação (page, size, sort)") Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar profissional por ID",
        description = "Retorna os dados de um profissional específico através do seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profissional encontrado",
            content = @Content(schema = @Schema(implementation = ProfissionalDTO.class))),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ProfissionalDTO> buscarPorId(
        @Parameter(description = "ID do profissional", required = true, example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar profissional",
        description = "Atualiza os dados de um profissional existente, incluindo especialidade e endereço."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profissional atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ProfissionalDTO.class))),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ProfissionalDTO> atualizar(
        @Parameter(description = "ID do profissional", required = true, example = "1") @PathVariable Long id,
        @RequestBody @Valid ProfissionalDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar profissional",
        description = "Remove um profissional do sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Profissional deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> remover(
        @Parameter(description = "ID do profissional", required = true, example = "1") @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
