package br.com.fiap.aposta_apoio.dto;

public record TokenDTO(
    String token,
    String tipo,
    Long expiresIn
) {}

