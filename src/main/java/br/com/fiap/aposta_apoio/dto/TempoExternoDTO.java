package br.com.fiap.aposta_apoio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TempoExternoDTO(String timezone, String datetime) {}
