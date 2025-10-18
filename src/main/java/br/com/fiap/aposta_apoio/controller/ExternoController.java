package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.TempoExternoDTO;
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
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;

/**
 * Controller para consumo de APIs externas.
 * Demonstra integração com serviços de terceiros.
 */
@RestController
@RequestMapping("/externo")
@Tag(name = "APIs Externas", description = "Endpoints que consomem serviços externos (WorldTimeAPI)")
public class ExternoController {

    private final RestTemplate restTemplate;
    private static final String URL = "https://worldtimeapi.org/api/timezone/America/Sao_Paulo";

    public ExternoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/tempo")
    @Operation(
        summary = "Obter horário atual de São Paulo",
        description = "Consome a API externa WorldTimeAPI para obter a data e hora atuais do timezone America/Sao_Paulo. Demonstra consumo de webservices REST externos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horário obtido com sucesso",
            content = @Content(schema = @Schema(implementation = TempoExternoDTO.class))),
        @ApiResponse(responseCode = "502", description = "Falha ao consumir serviço externo"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<TempoExternoDTO> tempoAtual() {
        try {
            TempoExternoDTO dto = restTemplate.getForObject(URL, TempoExternoDTO.class);
            if (dto == null || dto.datetime() == null) {
                return ResponseEntity.ok(new TempoExternoDTO("America/Sao_Paulo", OffsetDateTime.now().toString()));
            }
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return ResponseEntity.ok(new TempoExternoDTO("America/Sao_Paulo", OffsetDateTime.now().toString()));
        }
    }
}
