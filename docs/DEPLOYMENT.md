# CI/CD e deploy

O workflow [ci-cd.yml](../.github/workflows/ci-cd.yml) executa em pull requests e pushes para `main` e `develop`.
Em pushes, ele executa os testes, empacota a aplicacao e publica a imagem com a tag do commit no Docker Hub. O deploy para a VPS acontece apenas em pushes para `main`.

Configure estes secrets no repositorio GitHub:

- `DOCKERHUB_USERNAME` e `DOCKERHUB_TOKEN` (token de acesso do Docker Hub);
- `VPS_HOST`, `VPS_USER`, `VPS_SSH_PRIVATE_KEY` e `VPS_SSH_PORT`;
- `VPS_DEPLOY_PATH` (por exemplo, `/opt/vehicle-marketplace`).

Antes do primeiro deploy, instale Docker Engine com o plugin Docker Compose na VPS e crie `${VPS_DEPLOY_PATH}/.env`. Use `.env.example` como base e preencha, no minimo, `KEYCLOAK_SECRET`, `STRIPE_API_KEY`, `STRIPE_WEBHOOK_SECRET`, `RESEND_FROM` e `RESEND_API_TOKEN`. Esse arquivo permanece somente na VPS; o workflow envia apenas o compose e a tag imutavel da imagem.

O usuario da VPS precisa conseguir executar `docker compose` sem senha. Se o repositorio Docker Hub for privado, autentique a VPS uma vez com `docker login`.

## Acesso externo

O Compose publica a aplicacao (frontend e API) em `0.0.0.0:5173` por padrao. Depois do deploy, acesse:

```text
http://IP_DO_SERVIDOR:5173
```

Libere as portas TCP `5173` (aplicacao) e `8081` (Keycloak) tanto no firewall da VPS quanto no firewall/security list do provedor. Para usar outra porta, defina `APP_PORT` no `.env` da VPS.
