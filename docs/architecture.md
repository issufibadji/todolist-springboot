# Arquitetura do Projeto

## Visão geral

A aplicação segue a **arquitetura em camadas** clássica de aplicações Spring Boot, com separação clara de responsabilidades entre Controller, Service, Repository e Entity.

```
Cliente HTTP
     │
     ▼
┌─────────────────────┐
│   TodoController    │  ← Camada Web (REST)
│   /todos            │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│    TodoService      │  ← Camada de Negócio
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│   TodoRepository    │  ← Camada de Dados (Spring Data JPA)
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Banco de Dados     │  ← MySQL / H2
└─────────────────────┘
```

## Estrutura de pacotes

```
br.com.issufibadji.todolist/
├── TodolistApplication.java       # Entry point da aplicação
├── entity/
│   └── Todo.java                  # Entidade JPA mapeada para a tabela todos
├── exception/
│   └── BadRequestException.java   # Exceção customizada de domínio
├── repository/
│   └── TodoRepository.java        # Interface de acesso ao banco de dados
├── service/
│   └── TodoService.java           # Regras de negócio
└── web/
    ├── TodoController.java        # Endpoints REST
    └── GeneralExceptionHandler.java # Tratamento centralizado de erros
```

## Camadas

### Controller (`web/`)

Responsável por receber requisições HTTP, delegar para o `TodoService` e retornar as respostas com os códigos HTTP adequados.

- Anota com `@RestController` e `@RequestMapping`
- Valida o corpo da requisição com `@Valid`
- Retorna sempre a lista completa de todos após cada operação de escrita

### Service (`service/`)

Contém toda a lógica de negócio da aplicação.

- Verifica existência de registros antes de atualizar ou deletar
- Lança `BadRequestException` para erros de negócio (ex.: todo não encontrado)
- Define a ordenação padrão da listagem: **prioridade decrescente**, depois **id crescente**

### Repository (`repository/`)

Interface que estende `JpaRepository<Todo, Long>`, fornecendo automaticamente:

- `save()` — criação e atualização
- `findAll(Sort)` — listagem com ordenação
- `findById()` — busca por id
- `deleteById()` — remoção

Nenhuma query customizada é necessária para este projeto.

### Entity (`entity/`)

Classe `Todo` mapeada com JPA para a tabela `todos`. Usa validações Bean Validation (`@NotBlank`) que são verificadas antes de chegar ao banco.

### Tratamento de Erros

O `GeneralExceptionHandler` usa `@ControllerAdvice` para capturar exceções lançadas em qualquer camada e transformá-las em respostas HTTP padronizadas.

| Exceção | HTTP Status | Situação |
|---------|-------------|----------|
| `BadRequestException` | 400 Bad Request | Todo não encontrado em update/delete |
| Violação de `@NotBlank` | 400 Bad Request | Campo obrigatório vazio na criação |

## Decisões de design

**Lista completa como resposta de escrita:** Todas as operações POST, PUT e DELETE retornam a lista atualizada de todos. Isso simplifica o cliente, que não precisa fazer uma requisição GET adicional após cada mutação.

**Ordenação centralizada no Service:** A lógica de ordenação fica no `TodoService.list()`, chamado por todos os métodos de escrita, garantindo consistência sem duplicação de código.

**Exceção unchecked de domínio:** `BadRequestException` estende `RuntimeException`, evitando `try/catch` obrigatório no controller e mantendo o código limpo. O `@ControllerAdvice` cuida da tradução para HTTP.
