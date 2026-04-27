# Guia de Deploy

## Pré-requisitos

- Java 26 instalado no servidor
- MySQL 8.0+ acessível
- JAR gerado: `./mvnw clean package -DskipTests`

---

## Opção 1 — JAR direto no servidor (VPS/EC2)

### 1. Gerar o artefato

```bash
./mvnw clean package -DskipTests
```

O JAR será gerado em `target/todolist-0.0.1-SNAPSHOT.jar`.

### 2. Configurar variáveis de ambiente no servidor

Nunca suba credenciais no repositório. Configure via variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://seu-host:3306/todolist
export SPRING_DATASOURCE_USERNAME=seu_usuario
export SPRING_DATASOURCE_PASSWORD=sua_senha
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### 3. Executar

```bash
java -jar todolist-0.0.1-SNAPSHOT.jar
```

### 4. Executar como serviço systemd (Linux)

Crie o arquivo `/etc/systemd/system/todolist.service`:

```ini
[Unit]
Description=Todolist API
After=network.target

[Service]
User=ubuntu
ExecStart=/usr/bin/java -jar /opt/todolist/todolist-0.0.1-SNAPSHOT.jar
Environment="SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/todolist"
Environment="SPRING_DATASOURCE_USERNAME=root"
Environment="SPRING_DATASOURCE_PASSWORD=sua_senha"
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Ativar e iniciar:

```bash
sudo systemctl daemon-reload
sudo systemctl enable todolist
sudo systemctl start todolist
sudo systemctl status todolist
```

---

## Opção 2 — Docker

### Dockerfile

Crie um `Dockerfile` na raiz do projeto:

```dockerfile
FROM eclipse-temurin:26-jre-alpine
WORKDIR /app
COPY target/todolist-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build e execução

```bash
# Build da imagem
docker build -t todolist-api .

# Executar (passando credenciais via variáveis de ambiente)
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/todolist \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=sua_senha \
  --name todolist-api \
  todolist-api
```

### Docker Compose (API + MySQL juntos)

Crie um `docker-compose.yml`:

```yaml
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: senha123
      MYSQL_DATABASE: todolist
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  api:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/todolist
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: senha123
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    depends_on:
      - db

volumes:
  mysql_data:
```

Executar:

```bash
docker compose up -d
```

---

## Opção 3 — Railway / Render (PaaS)

Plataformas como [Railway](https://railway.app) e [Render](https://render.com) detectam o projeto automaticamente.

### Passos gerais

1. Conecte o repositório GitHub à plataforma
2. Configure as variáveis de ambiente no painel da plataforma:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
3. A plataforma executa `./mvnw package` e inicia o JAR automaticamente

### Railway — comando de start personalizado

```
java -jar target/todolist-0.0.1-SNAPSHOT.jar
```

---

## Verificar deploy

Após subir, teste os endpoints principais:

```bash
# Listar todos
curl https://seu-dominio.com/todos

# Criar tarefa
curl -X POST https://seu-dominio.com/todos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste","descricao":"Verificando deploy","realizado":false,"prioridade":1}'
```

Swagger disponível em: `https://seu-dominio.com/swagger-ui.html`

---

## Checklist pré-deploy

- [ ] Nenhuma senha ou credencial em `application.properties` no repositório
- [ ] `spring.jpa.hibernate.ddl-auto=update` configurado via variável de ambiente
- [ ] Banco de dados criado e acessível pelo servidor de produção
- [ ] Porta 8080 liberada no firewall/security group
- [ ] JAR gerado com `./mvnw clean package -DskipTests`
