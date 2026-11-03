# aula10

**Aula 10 - Integração Contínua (CI/CD) com Automação de Qualidade** (FATEC - 5º Semestre - Qualidade de Software).

Projeto Java 17 / Maven construído sobre a base da Aula 09 (`Calculadora`, `HeaderUtil`, `LoginService`, `NotificacaoService`) acrescido de persistência JDBC (`AuditoriaNotificacaoRepository`) validada em container PostgreSQL real via **TestContainers**.

## Estrutura das Atividades Implementadas

| Atividade / Etapa | Arquivos Principais | Recursos Implementados |
| --- | --- | --- |
| **Atividade 1: Pipeline CI (GitHub Actions)** | `.github/workflows/ci.yml`, `docs/ATIVIDADE1_GITHUB_ACTIONS.md` | Build Maven, Testes JUnit 5, Relatório JaCoCo publicado como artefato, Checkstyle, PMD, SonarQube Scan e Approval manual antes do deploy (`environment: staging`) |
| **Atividade 2: Pipeline CD (Jenkins)** | `Jenkinsfile`, `Dockerfile`, `k8s/deployment.yaml`, `docs/ATIVIDADE2_JENKINS_CD.md` | Build, Test + JUnit report, estágio de Análise Estática PMD, Deploy em Staging no branch `main`, Rollback automático (`kubectl rollout undo`) e alerta Slack |
| **Atividade 3: Automação de Qualidade** | `sonar-project.properties`, `config/sonarqube/quality-gate-setup.sh`, `docs/ATIVIDADE3_QUALITY_GATE.md` | Bloqueio de PRs com falhas, regras de cobertura mínima 80%, zero bugs críticos, debt técnico < 1h e simulação real de PR rejeitado por baixa cobertura (`teste/pr-baixa-cobertura`) |
| **Desafio Integrado & Segurança OWASP** | `.github/workflows/integrated-pipeline.yml`, `docs/DESAFIO_INTEGRADO_E_DISCUSSAO.md` | Execução paralela de Testes Unitários (cobertura > 85%), Testes de Integração com TestContainers PostgreSQL 16 e OWASP Dependency-Check antes do deploy |

## Comandos Locais

```bash
# Build, testes unitários, JaCoCo (>= 80%), Checkstyle, PMD e Javadoc
mvn -B clean package checkstyle:check pmd:check javadoc:javadoc

# Validar limite de cobertura do Desafio Integrado (> 85%)
mvn -B test jacoco:check -Djacoco.minimum.coverage=0.86

# Executar aplicação de demonstração
java -jar target/aula10_iqs-1.0.0.jar

# Executar testes de integração com TestContainers (requer Docker em execução)
mvn -B verify -Pintegration-tests
```
