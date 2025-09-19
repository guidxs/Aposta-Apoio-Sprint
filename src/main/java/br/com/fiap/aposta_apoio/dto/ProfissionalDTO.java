package br.com.fiap.aposta_apoio.dto;

import br.com.fiap.aposta_apoio.model.EnderecoVO;
import br.com.fiap.aposta_apoio.model.Especialidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfissionalDTO(
    Long id,
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String nome,
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,
    @NotNull(message = "Especialidade é obrigatória")
    Especialidade especialidade,
    EnderecoVO endereco
) {}
