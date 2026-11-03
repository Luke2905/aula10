# aula10

**Aula 10 - Integração Contínua (CI/CD) com Automação de Qualidade** (FATEC - 5º Semestre - Qualidade de Software).

Projeto Maven Java 17 utilizando como base os códigos da Aula 09 (`Main`, `Calculadora`, `HeaderUtil`, `LoginService` e `NotificacaoService`).

## Atividades Implementadas

1. **Atividade 1 - Pipeline CI com GitHub Actions (`.github/workflows/ci.yml`):**
   - Build com Maven (`mvn clean package`), Testes Unitários (`mvn test`), Checkstyle (`mvn checkstyle:check` com `continue-on-error: false`) e SonarQube Scan (`SonarSource/sonarqube-scan-action@master`).
   - Exercícios: geração e publicação do relatório JaCoCo (`mvn jacoco:report`) e approval manual antes do deploy (`environment: staging`).
2. **Atividade 2 - Pipeline CD com Jenkins (`Jenkinsfile`):**
   - Estágios `Build`, `Test` (com publicação `junit`), `Deploy to Staging` (no branch `main` com `Dockerfile` e `k8s/deployment.yaml`) e notificação `slackSend` em falha.
   - Exercícios: estágio de análise estática `PMD` (`mvn pmd:check`) e rollback automático (`kubectl rollout undo deployment/myapp`) em caso de falha no deploy.
   - Roteiro de configuração do Webhook no GitHub em `docs/ATIVIDADE2_JENKINS_CD.md`.
3. **Atividade 3 - Automação de Qualidade (`sonar-project.properties`, `.github/workflows/ci.yml`):**
   - Bloqueio de PRs com falhas (`Quality Gate` com `-Dsonar.qualitygate.wait=true` e `jacoco:check` >= 80%).
   - Regras de cobertura mínima 80%, zero bugs críticos e debt técnico < 1h (`config/sonarqube/quality-gate-setup.sh`).
   - Exercício: branch `teste/pr-baixa-cobertura` demonstrando a rejeição por baixa cobertura.
4. **Desafio Integrado e Tarefa Final (`.github/workflows/integrated-pipeline.yml`):**
   - Execução em paralelo dos testes unitários e dos testes de integração com **TestContainers** (`mvn verify -Pintegration-tests`).
   - Deploy condicionado à aprovação de todos os testes, cobertura > 85% e zero vulnerabilidades no **OWASP Dependency-Check**.
