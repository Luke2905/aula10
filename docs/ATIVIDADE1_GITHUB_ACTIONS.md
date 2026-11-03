# Atividade 1 - Pipeline CI com GitHub Actions

Arquivo implementado: `.github/workflows/ci.yml`.

## 1. Estágios do Pipeline (`build-and-test`)

Implementado exatamente conforme o roteiro da Aula 10:

- Checkout com `actions/checkout@v3` e configuração de JDK 17 (`temurin`) com `actions/setup-java@v3`.
- Build: `mvn clean package`.
- Testes unitários: `mvn test`.
- Relatório JaCoCo (Exercício): `mvn jacoco:report` e upload de `target/site/jacoco/` via `actions/upload-artifact@v4`.
- Estilo de código: `mvn checkstyle:check` com `continue-on-error: false`.
- Quality Gate SonarQube: `SonarSource/sonarqube-scan-action@master` com `SONAR_TOKEN` e `SONAR_HOST_URL` (executado quando os secrets do servidor SonarQube estão configurados no repositório).

## 2. Approval Manual antes do Deploy (Exercício)

O job `deploy` depende de `build-and-test` (`needs: build-and-test`) e referencia `environment: staging`. Para exigir aprovação humana no GitHub:

1. Acesse **Settings -> Environments -> staging**.
2. Habilite **Required reviewers** e selecione os aprovadores responsáveis.
3. Os deploys em `main` aguardarão revisão manual antes de executar os passos do estágio `deploy`.
