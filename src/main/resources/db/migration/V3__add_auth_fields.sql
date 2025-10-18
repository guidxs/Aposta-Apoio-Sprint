-- Adicionar campos de autenticação
ALTER TABLE usuario ADD COLUMN login VARCHAR(100) UNIQUE;
ALTER TABLE usuario ADD COLUMN senha VARCHAR(255);

-- Adicionar campo de role para controle de acesso
ALTER TABLE usuario ADD COLUMN role VARCHAR(50) DEFAULT 'USER';

-- Criar índice para melhorar performance de login
CREATE INDEX idx_usuario_login ON usuario(login);

