# Atividade 1 - Pipeline CI com GitHub Actions

Arquivo implementado: `.github/workflows/ci.yml`.

## 1. Estágios do Pipeline CI (`build-and-test`)

1. **Checkout e JDK 17:** utiliza `actions/checkout@v3` (com `fetch-depth: 0` para análise completa de histórico) e `actions/setup-java@v3` com distribuição Eclipse Temurin 17.
2. **Build Maven:** `mvn -B clean package`.
3. **Testes Unitários:** `mvn -B test` executando 47 casos JUnit 5.
4. **Relatório JaCoCo (Exercício 1):** executa `mvn -B jacoco:report` e publica `target/site/jacoco/` como artefato `jacoco-coverage-report` via `actions/upload-artifact@v4`.
5. **Verificação de Qualidade Estática:** `mvn -B checkstyle:check` (`continue-on-error: false`) e `mvn -B pmd:check`.
6. **Quality Gate com SonarQube:** utiliza `SonarSource/sonarqube-scan-action@master` lendo `SONAR_TOKEN` e `SONAR_HOST_URL` de `secrets`.

## 2. Approval Manual antes do Deploy (Exercício 2)

O job `deploy-staging` possui `needs: build-and-test` e declara o ambiente protegido `environment: staging`:

```yaml
  deploy-staging:
    name: Deploy to Staging
    needs: build-and-test
    if: github.event_name != 'pull_request' && github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    environment:
      name: staging
```

### Como ativar a aprovação humana obrigatória no GitHub

1. Acesse o repositório em **Settings -> Environments**.
2. Selecione (ou crie) o ambiente **`staging`**.
3. Marque **Required reviewers**, informe os revisores responsáveis pela liberação e clique em **Save protection rules**.
4. Sempre que um commit chegar em `main`, o job `deploy-staging` ficará pausado no status `Waiting` até a aprovação manual de um revisor autorizado.
