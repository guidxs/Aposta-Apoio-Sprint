package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.TempoExternoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/externo")
public class ExternoController {

    private final RestTemplate restTemplate;
    private static final String URL = "https://worldtimeapi.org/api/timezone/America/Sao_Paulo";

    public ExternoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/tempo")
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
