# Configuração e Execução

## Pré-requisitos

| Ferramenta | Versão mínima | Observação |
|------------|---------------|------------|
| Java JDK | 26 | Verifique com `java -version` |
| MySQL | 8.0+ | Necessário apenas para o profile de produção |
| Maven | 3.9+ | Opcional — o projeto inclui o Maven Wrapper (`mvnw`) |

## Clonar o repositório

```bash
git clone <url-do-repositorio>
cd todolist-desafio-backendjr
```

## Profiles de execução

O projeto possui três profiles configurados:

| Profile | Banco | Quando usar |
|---------|-------|-------------|
| padrão (sem profile) | MySQL | Produção |
| `dev` | H2 em memória | Desenvolvimento local sem MySQL |
| `test` | H2 em memória | Executado automaticamente nos testes |

---

## Executar em desenvolvimento (sem MySQL)

Usa o banco H2 em memória — nenhuma configuração adicional necessária.

```bash
# Linux / macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Windows (PowerShell)
.\mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"

# Windows (CMD)
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

Com o profile `dev` ativo, o console do H2 fica disponível em:

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:devdb
User: sa
Password: (vazio)
```

---

## Executar em produção (MySQL)

### 1. Criar o banco de dados

```sql
CREATE DATABASE todolist;
```

### 2. Configurar a conexão

Edite [src/main/resources/application.properties](../src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:mysql://localhost:8111/todolist
spring.datasource.username=root
spring.datasource.password=sua_senha_aqui
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

> Ajuste host, porta, usuário e senha conforme seu ambiente.

### 3. Gerar o artefato

```bash
# Linux / macOS
./mvnw clean package -DskipTests

# Windows
.\mvnw clean package -DskipTests
```

### 4. Executar o JAR

```bash
java -jar target/todolist-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em `http://localhost:8080`.

---

## Verificar se está rodando

```bash
curl http://localhost:8080/todos
```

Resposta esperada: `[]` (lista vazia) ou os todos cadastrados.

---

## Swagger UI

Acesse a documentação interativa da API em:

```
http://localhost:8080/swagger-ui.html
```

Permite testar todos os endpoints diretamente pelo navegador.

---

## Variáveis de ambiente (alternativa ao application.properties)

As propriedades do Spring Boot podem ser sobrescritas via variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/todolist
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=senha123

java -jar target/todolist-0.0.1-SNAPSHOT.jar
```

Isso é útil para deploys em containers (Docker, Kubernetes) sem alterar arquivos de configuração.
