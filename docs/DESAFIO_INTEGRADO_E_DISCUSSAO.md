# Desafio Integrado e Discussão

Arquivo implementado: `.github/workflows/integrated-pipeline.yml`.

## 1. Requisitos do Desafio Integrado

1. **Execução em paralelo (`needs: build`):**
   - `unit-tests`: executa testes unitários JUnit e valida cobertura > 85% (`-Djacoco.minimum.coverage=0.86`).
   - `integration-tests`: executa `mvn verify -Pintegration-tests` subindo container PostgreSQL real com **TestContainers** (`NotificacaoIntegrationIT`).
   - `owasp-dependency-check`: verifica vulnerabilidades com **OWASP Dependency-Check** (`--failOnCVSS 0.1`).
2. **Condição para Deploy:**
   - O job `deploy` possui `needs: [unit-tests, integration-tests, owasp-dependency-check]`, executando somente se todos os testes passarem, a cobertura for superior a 85% e não houver vulnerabilidades reportadas pelo OWASP Dependency-Check.

## 2. Discussão: GitHub Actions vs Jenkins

Para este projeto hospedado no GitHub, o **GitHub Actions** é mais direto por oferecer runners Ubuntu gerenciados já com Docker ativo (permitindo rodar TestContainers e OWASP Dependency-Check sem infraestrutura própria) e integração nativa com Pull Requests e proteção de ambientes (`environment: staging`). O **Jenkins** é indicado quando a organização exige infraestrutura própria *on-premises* ou acesso direto a clusters Kubernetes internos isolados.
