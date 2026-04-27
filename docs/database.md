# Banco de Dados

## Schema

A aplicação gerencia uma única tabela: **`todos`**.

O schema é gerado automaticamente pelo Hibernate com base na entidade `Todo`. A estrutura equivalente em SQL é:

```sql
CREATE TABLE todos (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    nome       VARCHAR(255) NOT NULL,
    descricao  VARCHAR(255) NOT NULL,
    realizado  BOOLEAN      NOT NULL DEFAULT FALSE,
    prioridade INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);
```

### Colunas

| Coluna | Tipo SQL | Restrição | Descrição |
|--------|----------|-----------|-----------|
| `id` | BIGINT | PK, NOT NULL, AUTO_INCREMENT | Identificador único |
| `nome` | VARCHAR(255) | NOT NULL | Nome da tarefa |
| `descricao` | VARCHAR(255) | NOT NULL | Descrição da tarefa |
| `realizado` | BOOLEAN | NOT NULL | Status de conclusão |
| `prioridade` | INT | NOT NULL | Nível de prioridade numérico |

---

## Configurações por ambiente

### Produção

**Arquivo:** [src/main/resources/application.properties](../src/main/resources/application.properties)

```properties
spring.datasource.url=jdbc:mysql://localhost:8111/todolist
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

- **Banco:** MySQL rodando na porta `8111`
- **DDL:** `update` — o Hibernate cria colunas/tabelas que faltam, mas **não remove** as existentes

### Desenvolvimento

**Arquivo:** [src/main/resources/application-dev.properties](../src/main/resources/application-dev.properties)

```properties
spring.datasource.url=jdbc:h2:mem:devdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

- **Banco:** H2 em memória (os dados são perdidos ao parar a aplicação)
- **DDL:** `create-drop` — cria o schema na inicialização e destrói ao encerrar
- **Console H2:** habilitado em `/h2-console`

### Testes

**Arquivo:** [src/test/resources/application.properties](../src/test/resources/application.properties)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
```

- **Banco:** H2 em memória isolado (`testdb`)
- Herda as demais configurações do contexto de teste do Spring Boot

---

## Estratégia DDL

| Valor | Comportamento | Quando usar |
|-------|---------------|-------------|
| `update` | Cria ou altera sem destruir dados | Produção |
| `create-drop` | Cria ao iniciar, destrói ao parar | Dev / Teste |
| `create` | Sempre recria do zero | Raramente (migrações manuais) |
| `validate` | Apenas valida sem alterar | Com Flyway/Liquibase |
| `none` | Nenhuma ação automática | Com Flyway/Liquibase |

> **Recomendação para produção:** use `validate` combinado com uma ferramenta de migração como **Flyway** ou **Liquibase** para controle de versão do schema.

---

## Dados de teste (fixtures)

Os testes de integração carregam dados via arquivos SQL.

**[src/test/resources/import.sql](../src/test/resources/import.sql)** — insere 5 todos de teste:

```sql
TRUNCATE TABLE todos;
INSERT INTO todos (nome, descricao, realizado, prioridade) VALUES ('@issufibadji', 'Curtir', false, 1);
INSERT INTO todos (nome, descricao, realizado, prioridade) VALUES ('@issufibadji', 'Comentar', false, 1);
INSERT INTO todos (nome, descricao, realizado, prioridade) VALUES ('@issufibadji', 'Compartilhar', false, 1);
INSERT INTO todos (nome, descricao, realizado, prioridade) VALUES ('@issufibadji', 'Se Inscrever', false, 1);
INSERT INTO todos (nome, descricao, realizado, prioridade) VALUES ('@issufibadji', 'Ativar as Notificações', false, 1);
```

**[src/test/resources/remove.sql](../src/test/resources/remove.sql)** — limpa a tabela após os testes:

```sql
TRUNCATE TABLE todos;
```
