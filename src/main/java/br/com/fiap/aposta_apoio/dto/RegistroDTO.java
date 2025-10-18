package br.com.fiap.aposta_apoio.dto;

import br.com.fiap.aposta_apoio.model.EnderecoVO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegistroDTO(
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    String email,

    @NotBlank(message = "Telefone é obrigatório")
    String telefone,

    @NotBlank(message = "CPF é obrigatório")
    String cpf,

    @NotNull(message = "Data de nascimento é obrigatória")
    LocalDate dataNascimento,

    @NotNull(message = "Endereço é obrigatório")
    EnderecoVO endereco,

    @NotBlank(message = "Login é obrigatório")
    String login,

    @NotBlank(message = "Senha é obrigatória")
    String senha,

    String role
) {}

