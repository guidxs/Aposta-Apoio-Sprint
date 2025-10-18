package br.com.fiap.aposta_apoio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EnderecoVO {
    @NotBlank
    @Size(max = 100)
    private String rua;

    @NotBlank
    @Size(max = 10)
    private String numero;

    @NotBlank
    @Size(max = 50)
    private String bairro;

    @NotBlank
    @Size(max = 50)
    private String cidade;

    @NotBlank
    @Size(max = 2)
    private String estado;

    @NotBlank
    @Size(max = 10)
    private String cep;
}
