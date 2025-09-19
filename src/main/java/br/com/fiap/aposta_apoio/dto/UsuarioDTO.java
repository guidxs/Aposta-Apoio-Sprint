package br.com.fiap.aposta_apoio.dto;

import br.com.fiap.aposta_apoio.model.EnderecoVO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UsuarioDTO(
    Long id,
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String nome,
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,
    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 8, max = 20, message = "Telefone deve ter entre 8 e 20 caracteres")
    String telefone,
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    String cpf,
    @NotNull(message = "Data de nascimento é obrigatória")
    LocalDate dataNascimento,
    EnderecoVO endereco
) {}
