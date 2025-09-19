-- Adiciona índice para consultas futuras por especialidade
ALTER TABLE profissional ADD INDEX idx_profissional_especialidade (especialidade);

