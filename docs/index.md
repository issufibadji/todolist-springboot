# Documentação — Todolist API

Bem-vindo à documentação completa do projeto **Todolist API**, uma API REST para gerenciamento de tarefas (CRUD) construída com Spring Boot.

## Índice de documentos

| Documento | Descrição |
| --- | --- |
| [Visão Geral e Arquitetura](architecture.md) | Estrutura em camadas, fluxo de dados e decisões de design |
| [Referência da API](api.md) | Todos os endpoints REST com exemplos de request/response |
| [Configuração e Execução](setup.md) | Pré-requisitos, instalação, profiles e execução local |
| [Banco de Dados](database.md) | Schema, configurações por ambiente e estratégias DDL |
| [Testes](testing.md) | Estratégia de testes, cobertura e como executar |
| [Deploy](deploy.md) | JAR direto, Docker, Docker Compose e plataformas PaaS |

## Visão rápida

- **Linguagem:** Java 26
- **Framework:** Spring Boot 3.5.14
- **Banco de dados:** MySQL (produção) / H2 (desenvolvimento e testes)
- **Documentação interativa:** Swagger UI em `/swagger-ui.html`
- **Porta padrão:** 8080

## Links úteis

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console (dev): `http://localhost:8080/h2-console`
- Base URL da API: `http://localhost:8080/todos`
