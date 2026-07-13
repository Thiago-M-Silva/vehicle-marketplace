# Horse Power Vehicles — Vehicle Marketplace (Backend)

Projeto backend em **Quarkus 3.21.1** e **Java 21** que oferece API REST para um marketplace de veículos (carros, motos, barcos, aviões etc.).

Resumo rápido: aplicação monolítica com integrações externas (PostgreSQL, MongoDB, Keycloak, Stripe) e UI de referência em [src/main/resources/webui].

## Pré-requisitos

- Java 21
- Maven (ou usar o wrapper `./mvnw` / `mvnw.cmd`)
- Node.js (para a web UI, opcional)
- Docker & Docker Compose (para ambientes locais com dependências)

## Instalação e execução em desenvolvimento

1. Clone o repositório e entre na pasta:

```bash
git clone https://github.com/Thiago-M-Silva/vehicle-marketplace.git
cd vehicle-marketplace
```

2. Copie o arquivo de exemplo de variáveis de ambiente e ajuste conforme necessário:

```bash
cp .env.example .env
# (no Windows PowerShell use: Copy-Item .env.example .env)
```

3. Executar em modo desenvolvimento (Quarkus Dev):

```bash
./mvnw quarkus:dev
```

A aplicação ficará disponível em http://localhost:8080. A documentação OpenAPI aparece em `/q/swagger-ui/`.

## Comandos úteis

- Build (jar):

```bash
./mvnw package -DskipTests
```

- Empacotar imagem Docker (por projeto):

```bash
./mvnw -Dquarkus.container-image.build=true package
```

- Rodar testes unitários:

```bash
./mvnw test
```

## Variáveis de ambiente importantes

- `DATABASE_URL` / `QUARKUS_DATASOURCE_URL` — string de conexão PostgreSQL
- `MONGODB_URI` — string de conexão MongoDB
- `KEYCLOAK_URL`, `KEYCLOAK_REALM`, `KEYCLOAK_CLIENT_ID` — Keycloak (auth)
- `STRIPE_API_KEY` — chave da API Stripe (modo teste)

Verifique `.env.example` para a lista completa e valores recomendados para desenvolvimento.

## Docker / docker-compose

O projeto inclui um `docker-compose.yml` para subir serviços dependentes localmente (Postgres, MongoDB, Keycloak). Exemplo:

```bash
docker-compose up --build
```

Use logs e `docker-compose down` para parar e remover containers.

## Testes e troubleshooting

- Logs de testes ficam em `target/surefire-reports/` para inspeção de falhas.
- Se o build nativo falhar, tente executar sem o perfil nativo e com `-DskipTests` para isolar problemas.

## Estrutura do projeto

Principais pastas:

- `src/main/java` — código-fonte Java (controllers, services, repositories)
- `src/main/resources` — migrations, arquivos estáticos e `application.properties`
- `src/test/java` — testes JUnit

## Contribuição

1. Abra uma issue descrevendo o que deseja implementar.
2. Crie uma branch com nome descritivo.
3. Abra um PR com descrição e testes quando aplicável.

## Contato e referências

- Wiki do projeto: https://github.com/Thiago-M-Silva/vehicle-marketplace/wiki
