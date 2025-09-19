package br.com.fiap.aposta_apoio.controller;

import br.com.fiap.aposta_apoio.dto.ResumoDTO;
import br.com.fiap.aposta_apoio.repository.UsuarioRepository;
import br.com.fiap.aposta_apoio.repository.ProfissionalRepository;
import br.com.fiap.aposta_apoio.repository.SessaoApoioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
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
    public ResponseEntity<ResumoDTO> resumo() {
        ResumoDTO dto = new ResumoDTO(
                usuarioRepository.count(),
                profissionalRepository.count(),
                sessaoApoioRepository.count()
        );
        return ResponseEntity.ok(dto);
    }
}

