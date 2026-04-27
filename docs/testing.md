# Testes

## Estratégia

O projeto utiliza **testes de integração** que sobem o contexto completo do Spring Boot e fazem requisições HTTP reais contra a aplicação, usando banco H2 em memória. Não há mocks de camada de dados — os testes exercitam o fluxo completo: Controller → Service → Repository → H2.

## Tecnologias

| Biblioteca | Papel |
|------------|-------|
| Spring Boot Test | Sobe o contexto completo da aplicação (`@SpringBootTest`) |
| WebTestClient | Cliente HTTP reativo para disparar requisições nos testes |
| Spring WebFlux (test scope) | Provê o `WebTestClient` |
| H2 Database | Banco em memória isolado para os testes |
| `@Sql` | Carrega fixtures SQL antes/depois de cada teste |

## Configuração dos testes

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodolistApplicationTests {
    @Autowired
    private WebTestClient webTestClient;
}
```

- `RANDOM_PORT` — a aplicação sobe em uma porta aleatória, evitando conflito com instâncias locais
- O banco de dados usado é o H2 em memória (`testdb`), definido em [src/test/resources/application.properties](../src/test/resources/application.properties)

## Casos de teste

### Criação (`POST /todos`)

| Teste | Entrada | Resultado esperado |
|-------|---------|-------------------|
| `testCreateTodoSuccess` | Todo com `nome` e `descricao` válidos | HTTP 201, lista com 1 item correspondente |
| `testCreateTodoFailure` | Todo com `nome` e `descricao` vazios | HTTP 400 (violação de `@NotBlank`) |

### Atualização (`PUT /todos/{id}`)

| Teste | Pré-condição | Entrada | Resultado esperado |
|-------|-------------|---------|-------------------|
| `testUpdateTodoSuccess` | 5 todos carregados via `import.sql` | Todo atualizado para id existente | HTTP 200, lista com 5 itens, primeiro item reflete a atualização |
| `testUpdateTodoFailure` | Banco vazio | Id inexistente | HTTP 400, mensagem "Todo {id} não existe!" |

### Remoção (`DELETE /todos/{id}`)

| Teste | Pré-condição | Entrada | Resultado esperado |
|-------|-------------|---------|-------------------|
| `testDeleteTodoSuccess` | 5 todos carregados via `import.sql` | Id existente | HTTP 200, lista com 4 itens |
| `testDeleteTodoFailure` | Banco vazio | Id inexistente | HTTP 400, mensagem "Todo {id} não existe!" |

### Listagem (`GET /todos`)

| Teste | Pré-condição | Resultado esperado |
|-------|-------------|-------------------|
| `testListTodos` | 5 todos carregados via `import.sql` | HTTP 200, lista com exatamente 5 todos, ordenados por prioridade DESC e id ASC |

## Executar os testes

```bash
# Linux / macOS
./mvnw test

# Windows (PowerShell)
.\mvnw test

# Windows (CMD)
mvnw.cmd test
```

Saída esperada ao final:

```
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Dados de teste (`TestConstants`)

A classe `TestConstants` centraliza os dados usados nas asserções:

```java
public static final List<Todo> TODOS = List.of(
    new Todo(9995L, "@issufibadji", "Curtir",                  false, 1),
    new Todo(9996L, "@issufibadji", "Comentar",                false, 1),
    new Todo(9997L, "@issufibadji", "Compartilhar",            false, 1),
    new Todo(9998L, "@issufibadji", "Se Inscrever",            false, 1),
    new Todo(9999L, "@issufibadji", "Ativar as Notificações",  false, 1)
);
```

Os IDs `9995–9999` são definidos explicitamente no `import.sql` para garantir determinismo nas asserções de ordem e id.

## Cobertura

| Funcionalidade | Coberta? |
|----------------|----------|
| Criar todo válido | Sim |
| Criar todo inválido (campos vazios) | Sim |
| Atualizar todo existente | Sim |
| Atualizar todo inexistente | Sim |
| Deletar todo existente | Sim |
| Deletar todo inexistente | Sim |
| Listar todos com ordenação correta | Sim |
| Ordenação por prioridade DESC + id ASC | Sim (implícito no `testListTodos`) |
