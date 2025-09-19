package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.ProfissionalDTO;
import br.com.fiap.aposta_apoio.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfissionalDTO> criar(@RequestBody @Valid ProfissionalDTO dto) {
        ProfissionalDTO criado = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/profissionais/" + criado.id()))
                .body(criado);
    }

    @GetMapping
    public ResponseEntity<Page<ProfissionalDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProfissionalDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
