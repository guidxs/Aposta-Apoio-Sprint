package br.com.fiap.aposta_apoio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record SessaoApoioDTO(
    Long id,
    @NotNull(message = "ID do usuário é obrigatório")
    Long usuarioId,
    @NotNull(message = "ID do profissional é obrigatório")
    Long profissionalId,
    @NotNull(message = "Data e hora são obrigatórios")
    LocalDateTime dataHora,
    @NotBlank(message = "Descrição é obrigatória")
    String descricao
) {}
