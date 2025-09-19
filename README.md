# ApostaApoio - API de Apoio a Pessoas com Vício em Apostas

**Grupo:**
- Guilherme Doretto Sobreiro - RM: 99674
- Guilherme Fazito Ziolli Sordili - RM: 550539
- Raí Gumieri dos Santos - RM: 98287

## 1. Descrição
API REST para cadastro de usuários (pessoas em acompanhamento), profissionais de apoio e sessões de apoio, incluindo:
- CRUD completo com validações.
- Paginação e ordenação nativas (Spring Data Pageable).
- Uso de DTOs (records), VO (EnderecoVO), Enum (Especialidade).
- Migrações versionadas com Flyway.
- Tratamento centralizado de erros (RestControllerAdvice) com respostas JSON padronizadas.
- Consumo de serviço externo (horário /externo/tempo) com fallback resiliente.
- Endpoint de resumo (/dashboard/resumo) com estatísticas agregadas.

## 2. Arquitetura e Boas Práticas
Camadas:
- controller: Exposição de endpoints REST.
- service: Regras de negócio e orquestração.
- repository: Acesso a dados (Spring Data JPA).
- model: Entidades JPA + VO + Enum.
- dto: Objetos de transporte (records) desacoplando a API do modelo interno.
- exception: Tratamento de erros padronizado.

Padrões e recursos:
- DTO (record) evita exposição direta de entidades.
- VO (EnderecoVO) como objeto de valor embarcado (@Embedded).
- Enum (Especialidade) persistido como String.
- Migrações Flyway garantem reprodutibilidade do schema.
- Respostas HTTP adequadas (201 Created + Location em POST, 204 em DELETE, 400/409/502 conforme contexto).

## 2.1 Diagramas

- Diagrama de Arquitetura

<p align="center">
  <img width="1621" height="791" alt="DiagramaArquitetura" src="https://github.com/user-attachments/assets/c3cdd4b2-9c65-4d15-9176-586eb2917068" />
</p>

- Diagrama de Entidades

<p align="center">
  <img width="781" height="571" alt="DiagramaEntidade" src="https://github.com/user-attachments/assets/b8a993fb-296b-4992-8455-89ffe161ef1f" />
</p>

- Caso de Uso

<p align="center">
  <img width="1006" height="791" alt="CasodeUso" src="https://github.com/user-attachments/assets/116ff2a8-6190-48d8-8a77-3a2391a70981" />
</p>

## 2.2 Estrutura de Pastas
```
ApostaApoio/
  pom.xml
  README.md
  src/
    main/
      java/
        br/com/fiap/aposta_apoio/
          ApostaApoioApplication.java          # Classe principal / configuração
          controller/                          # Endpoints REST
            UsuarioController.java
            ProfissionalController.java
            SessaoApoioController.java
            DashboardController.java
            ExternoController.java
          service/                             # Regras de negócio
            UsuarioService.java
            ProfissionalService.java
            SessaoApoioService.java
          repository/                          # Repositórios Spring Data
            UsuarioRepository.java
            ProfissionalRepository.java
            SessaoApoioRepository.java
          model/                               # Entidades + VO + Enum
            Usuario.java
            Profissional.java
            SessaoApoio.java
            EnderecoVO.java
            Especialidade.java
          dto/                                 # DTOs (records)
            UsuarioDTO.java
            ProfissionalDTO.java
            SessaoApoioDTO.java
            ResumoDTO.java
            TempoExternoDTO.java
          exception/                           # Tratamento centralizado de erros
            GlobalExceptionHandler.java
    resources/
      application.properties                   # Configurações
      db/migration/                            # Migrações Flyway
        V1__create_tables.sql
        V2__add_index_profissional_especialidade.sql
```

## 3. Tecnologias
- Java 21
- Spring Boot (Web, Data JPA, Validation)
- MySQL 8
- Flyway
- Lombok
- Maven

## 4. Requisitos de Ambiente
| Item | Versão Sugerida |
|------|-----------------|
| Java | 21 |
| MySQL| 8.x |
| Maven| 3.8+ |

## 5. Configuração
application.properties (exemplo):
```
spring.application.name=ApostaApoio
spring.datasource.url=jdbc:mysql://localhost:3306/aposta_apoio?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
server.port=8080
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## 6. Migrações Flyway
Arquivos em `src/main/resources/db/migration/`:
- V1__create_tables.sql: Criação das tabelas base (usuario, profissional, sessao_apoio).
- V2__add_index_profissional_especialidade.sql: Índice em profissional.especialidade.

Para recriar do zero:
```
DROP DATABASE aposta_apoio;
CREATE DATABASE aposta_apoio;
```
Subir a aplicação para reaplicar migrações.

## 7. Execução
```
mvn clean install
mvn spring-boot:run
```
Ou pela IDE executando `ApostaApoioApplication`.

## 8. Endpoints Principais
| Recurso | Método | Caminho | Descrição | Suporte Paginação |
|---------|--------|---------|-----------|-------------------|
| Usuários | POST | /usuarios | Cria usuário | - |
| Usuários | GET | /usuarios | Lista usuários | Sim (page,size,sort) |
| Usuários | GET | /usuarios/{id} | Detalhe | - |
| Usuários | PUT | /usuarios/{id} | Atualiza | - |
| Usuários | DELETE | /usuarios/{id} | Remove (se sem sessões) | - |
| Profissionais | POST | /profissionais | Cria profissional | - |
| Profissionais | GET | /profissionais | Lista profissionais | Sim |
| Profissionais | GET | /profissionais/{id} | Detalhe | - |
| Profissionais | PUT | /profissionais/{id} | Atualiza | - |
| Profissionais | DELETE | /profissionais/{id} | Remove | - |
| Sessões | POST | /sessoes | Cria sessão (vínculo usuário+profissional) | - |
| Sessões | GET | /sessoes | Lista sessões | Sim |
| Sessões | GET | /sessoes/{id} | Detalhe | - |
| Sessões | PUT | /sessoes/{id} | Atualiza | - |
| Sessões | DELETE | /sessoes/{id} | Remove | - |
| Dashboard | GET | /dashboard/resumo | Totais agregados | - |
| Externo | GET | /externo/tempo | Consulta horário (API externa + fallback) | - |

### Paginação e Ordenação
Parâmetros padrão Spring:
- `page` (0-based)
- `size` (tamanho da página)
- `sort=campo,asc|desc`
Exemplo:
```
GET /usuarios?page=0&size=5&sort=nome,asc
```

## 9. Exemplos de Requisição
### POST /usuarios
```
{
  "nome": "Joao Silva",
  "email": "joao.silva@example.com",
  "telefone": "11988887777",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-10",
  "endereco": {
    "rua": "Rua das Flores",
    "numero": "123",
    "bairro": "Centro",
    "cidade": "Sao Paulo",
    "estado": "SP",
    "cep": "01001000"
  }
}
```
Resposta (201 Created + Location):
```
{
  "id": 1,
  "nome": "Joao Silva",
  "email": "joao.silva@example.com",
  "telefone": "11988887777",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-10",
  "endereco": { ... }
}
```

### POST /profissionais
```
{
  "nome": "Ana Terapeuta",
  "email": "ana.terapeuta@example.com",
  "especialidade": "PSICOLOGIA",
  "endereco": {
    "rua": "Rua Saude",
    "numero": "10",
    "bairro": "Clinicas",
    "cidade": "Campinas",
    "estado": "SP",
    "cep": "13010000"
  }
}
```
Enum Especialidade permitido: `PSICOLOGIA, ORIENTACAO, TERAPIA_GRUPO, COACHING, PSIQUIATRIA`.

### POST /sessoes
```
{
  "usuarioId": 1,
  "profissionalId": 1,
  "dataHora": "2025-09-25T09:00:00",
  "descricao": "Sessao inicial"
}
```

### GET /dashboard/resumo
```
{
  "totalUsuarios": 3,
  "totalProfissionais": 2,
  "totalSessoes": 5
}
```

### GET /externo/tempo
```
{
  "timezone": "America/Sao_Paulo",
  "datetime": "2025-09-18T20:56:58.558543-03:00"
}
```

## 10. Tratamento de Erros (Formato)
Estrutura padrão:
```
{
  "timestamp": "2025-09-18T20:30:00",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Erro de validação",
  "erros": [
    { "campo": "dataNascimento", "mensagem": "Data de nascimento é obrigatória" }
  ]
}
```
Casos especiais:
- 409: Conflito (ex: exclusão de usuário com sessões).
- 502: Falha em serviço externo.
- 400: Enum inválido ou formato de data incorreto.

## 11. Consumo de Serviço Externo
Endpoint `/externo/tempo` consome `worldtimeapi.org`. Em caso de falha retorna fallback com horário local do servidor.


## 12. Testes Rápidos (curl)
Criar usuário:
```
curl -i -X POST http://localhost:8080/usuarios -H "Content-Type: application/json" -d "{\"nome\":\"Joao Silva\",\"email\":\"joao.silva@example.com\",\"telefone\":\"11988887777\",\"cpf\":\"12345678901\",\"dataNascimento\":\"1990-05-10\",\"endereco\":{\"rua\":\"Rua das Flores\",\"numero\":\"123\",\"bairro\":\"Centro\",\"cidade\":\"Sao Paulo\",\"estado\":\"SP\",\"cep\":\"01001000\"}}"
```
Listar usuários paginados:
```
curl -s "http://localhost:8080/usuarios?page=0&size=5&sort=nome,asc"
```
Consumir tempo externo:
```
curl -s http://localhost:8080/externo/tempo
```
