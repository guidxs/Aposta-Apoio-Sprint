package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.SessaoApoioDTO;
import br.com.fiap.aposta_apoio.service.SessaoApoioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sessoes")
public class SessaoApoioController {
    private final SessaoApoioService service;

    public SessaoApoioController(SessaoApoioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SessaoApoioDTO> criar(@RequestBody @Valid SessaoApoioDTO dto) {
        SessaoApoioDTO criado = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/sessoes/" + criado.id()))
                .body(criado);
    }

    @GetMapping
    public ResponseEntity<Page<SessaoApoioDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoApoioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessaoApoioDTO> atualizar(@PathVariable Long id, @RequestBody @Valid SessaoApoioDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
