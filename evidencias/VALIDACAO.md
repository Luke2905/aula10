# Validação Real - Aula 10 (CI/CD com Automação de Qualidade)

## 1. Ambiente Local

- Build e verificações locais: `mvn clean package checkstyle:check pmd:check jacoco:check -Djacoco.minimum.coverage=0.86` -> **BUILD SUCCESS** (Maven 3.9.16, JDK 21.0.11 com `--release 17`).
- Classes base herdadas da Aula 09 (`Main`, `Calculadora`, `HeaderUtil`, `LoginService`, `NotificacaoService`): **43 testes unitários executados, 0 falhas, 0 erros**.
- `Checkstyle`: **0 violações** (`config/checkstyle/checkstyle.xml`).
- `PMD`: **0 violações** (`config/pmd/ruleset.xml`).
- `JAR` executável (`target/aula10_iqs-1.0.0.jar`) validado em console.

## 2. Cobertura JaCoCo na `main`

| Classe | Linhas Cobertas | Linhas Totais | Branches Cobertos | Branches Totais |
| --- | --- | --- | --- | --- |
| `HeaderUtil` | 3 | 3 (100%) | 2 | 2 (100%) |
| `LoginService` | 3 | 3 (100%) | 6 | 6 (100%) |
| `NotificacaoService` | 6 | 6 (100%) | 4 | 4 (100%) |
| `Calculadora` | 5 | 5 (100%) | 2 | 2 (100%) |
| `Main` | 16 | 20 (80%) | 0 | 0 (100%) |
| **Total do Bundle** | **33** | **37 (89,19%)** | **14** | **14 (100%)** |

Atende simultaneamente às regras da **Atividade 3** (`>= 80%`) e do **Desafio Integrado** (`> 85%`).

## 3. Execuções Reais no GitHub Actions (`Luke2905/aula10`)

- **Java CI Pipeline (`main` - commit `3b46b4f`):** [Run #41691067582](https://github.com/Luke2905/aula10/actions/runs/41691067582) -> **SUCCESS** (`build-and-test` e `deploy` aprovados, artefato `jacoco-report` publicado).
- **Integrated CI/CD Pipeline (`main` - commit `3b46b4f`):** [Run #41691067485](https://github.com/Luke2905/aula10/actions/runs/41691067485) -> **SUCCESS** (`build` aprovado; `unit-tests`, `integration-tests` com **TestContainers PostgreSQL 16** e `owasp-dependency-check` executados em paralelo e aprovados; `deploy` aprovado).
- **Rejeição por Baixa Cobertura (`teste/pr-baixa-cobertura` - commit `512afb6`):**
  - [Java CI Pipeline - Run #41691293583](https://github.com/Luke2905/aula10/actions/runs/41691293583) -> **FAILURE** (`Quality Gate` e `build-and-test` reprovados com cobertura de linhas `0.08 < 0.80`; `deploy` bloqueado/skipped).
  - [Integrated CI/CD Pipeline - Run #41691293414](https://github.com/Luke2905/aula10/actions/runs/41691293414) -> **FAILURE** (`unit-tests` reprovado por cobertura insuficiente; `deploy` bloqueado/skipped).

## 4. Transparência sobre Serviços Externos e Aprovações Humanas

- Não há servidor SonarQube ou Jenkins externo provisionado neste ambiente; os arquivos `Jenkinsfile`, `sonar-project.properties` e `config/sonarqube/quality-gate-setup.sh` contêm exatamente a configuração pedida na Aula 10, sem inventar execuções de servidores inexistentes.
- Para abrir e visualizar o PR rejeitado no navegador: `https://github.com/Luke2905/aula10/compare/main...teste/pr-baixa-cobertura`.
- Para ativar o bloqueio humano no `environment: staging`: configurar *Required reviewers* em `Settings -> Environments -> staging` no GitHub.
