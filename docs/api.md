# Referência da API

**Base URL:** `http://localhost:8080`
**Formato:** JSON
**Documentação interativa:** `http://localhost:8080/swagger-ui.html`

---

## Modelo de dados — Todo

```json
{
  "id": 1,
  "nome": "Estudar Spring Boot",
  "descricao": "Revisar anotações do curso",
  "realizado": false,
  "prioridade": 2
}
```

| Campo | Tipo | Obrigatório | Descrição |
| --- | --- | --- | --- |
| `id` | Long | Não (gerado) | Identificador único, auto-incremento |
| `nome` | String | Sim | Nome da tarefa (não pode ser vazio) |
| `descricao` | String | Sim | Descrição da tarefa (não pode ser vazia) |
| `realizado` | Boolean | Não | Status de conclusão (padrão: `false`) |
| `prioridade` | Integer | Não | Nível de prioridade (maior = mais urgente) |

---

## Endpoints

### POST /todos — Criar tarefa

Cria uma nova tarefa e retorna a lista completa de todos, ordenada.

Requisição:

```http
POST /todos
Content-Type: application/json
```

```json
{
  "nome": "Estudar Spring Boot",
  "descricao": "Revisar anotações do curso",
  "realizado": false,
  "prioridade": 2
}
```

Resposta de sucesso — 201 Created:

```json
[
  {
    "id": 1,
    "nome": "Estudar Spring Boot",
    "descricao": "Revisar anotações do curso",
    "realizado": false,
    "prioridade": 2
  }
]
```

Resposta de erro — 400 Bad Request (campos obrigatórios vazios):

```text
nome: must not be blank
```

---

### GET /todos — Listar tarefas

Retorna todas as tarefas ordenadas por **prioridade decrescente** e, em caso de empate, por **id crescente**.

Requisição:

```http
GET /todos
```

Resposta de sucesso — 200 OK:

```json
[
  {
    "id": 2,
    "nome": "Fazer deploy",
    "descricao": "Publicar versão em produção",
    "realizado": false,
    "prioridade": 5
  },
  {
    "id": 1,
    "nome": "Estudar Spring Boot",
    "descricao": "Revisar anotações do curso",
    "realizado": false,
    "prioridade": 2
  }
]
```

---

### PUT /todos/{id} — Atualizar tarefa

Atualiza uma tarefa existente pelo `id` e retorna a lista completa atualizada.

Requisição:

```http
PUT /todos/1
Content-Type: application/json
```

```json
{
  "nome": "Estudar Spring Boot",
  "descricao": "Revisar anotações e fazer exercícios",
  "realizado": true,
  "prioridade": 2
}
```

Resposta de sucesso — 200 OK:

```json
[
  {
    "id": 1,
    "nome": "Estudar Spring Boot",
    "descricao": "Revisar anotações e fazer exercícios",
    "realizado": true,
    "prioridade": 2
  }
]
```

Resposta de erro — 400 Bad Request (id não encontrado):

```text
Todo 99 não existe!
```

---

### DELETE /todos/{id} — Deletar tarefa

Remove uma tarefa pelo `id` e retorna a lista restante.

Requisição:

```http
DELETE /todos/1
```

Resposta de sucesso — 200 OK:

```json
[]
```

Resposta de erro — 400 Bad Request (id não encontrado):

```text
Todo 99 não existe!
```

---

### POST /todos/batch — Criar múltiplas tarefas

Cria várias tarefas em uma única requisição e retorna a lista completa ordenada.

Requisição:

```http
POST /todos/batch
Content-Type: application/json
```

```json
[
  { "nome": "Tarefa A", "descricao": "Descrição A", "realizado": false, "prioridade": 2 },
  { "nome": "Tarefa B", "descricao": "Descrição B", "realizado": false, "prioridade": 5 }
]
```

Resposta de sucesso — 201 Created:

```json
[
  {
    "id": 2,
    "nome": "Tarefa B",
    "descricao": "Descrição B",
    "realizado": false,
    "prioridade": 5
  },
  {
    "id": 1,
    "nome": "Tarefa A",
    "descricao": "Descrição A",
    "realizado": false,
    "prioridade": 2
  }
]
```

---

## Exemplos com curl

```bash
# Criar
curl -s -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Tarefa 1","descricao":"Descrição","realizado":false,"prioridade":1}'

# Criar em batch
curl -s -X POST http://localhost:8080/todos/batch \
  -H "Content-Type: application/json" \
  -d '[{"nome":"Tarefa 1","descricao":"Desc 1","prioridade":1},{"nome":"Tarefa 2","descricao":"Desc 2","prioridade":2}]'

# Listar
curl -s http://localhost:8080/todos

# Atualizar
curl -s -X PUT http://localhost:8080/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Tarefa 1","descricao":"Atualizada","realizado":true,"prioridade":1}'

# Deletar
curl -s -X DELETE http://localhost:8080/todos/1
```

---

## Exemplos com httpie

```bash
# Criar
http POST :8080/todos nome="Tarefa 1" descricao="Descrição" realizado:=false prioridade:=1

# Criar em batch
http POST :8080/todos/batch \
  '[{"nome":"Tarefa 1","descricao":"Desc 1","prioridade":1},{"nome":"Tarefa 2","descricao":"Desc 2","prioridade":2}]'

# Listar
http GET :8080/todos

# Atualizar
http PUT :8080/todos/1 nome="Tarefa 1" descricao="Atualizada" realizado:=true prioridade:=1

# Deletar
http DELETE :8080/todos/1
```

---

## Códigos de resposta

| Código | Situação |
| --- | --- |
| 200 OK | Listagem, atualização ou remoção com sucesso |
| 201 Created | Tarefa criada com sucesso (POST /todos e POST /todos/batch) |
| 400 Bad Request | Validação falhou ou recurso não encontrado |
