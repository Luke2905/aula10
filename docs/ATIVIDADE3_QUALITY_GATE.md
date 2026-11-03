# Atividade 3 - Automação de Qualidade

Arquivos relacionados:
- `.github/workflows/ci.yml` (job `sonarqube` / `Quality Gate`)
- `sonar-project.properties`
- `config/sonarqube/quality-gate-setup.sh`

## 1. Bloqueio de PRs e Regras do Quality Gate

Regras definidas na Atividade 3:
- **Cobertura mínima:** 80% (`jacoco:check` com `0.80` no `pom.xml` e `metric=coverage < 80` no SonarQube).
- **Zero bugs críticos:** `critical_violations > 0` no SonarQube e `checkstyle:check` / `pmd:check` sem erros.
- **Debt técnico < 1h:** `sqale_index > 59` (minutos) no script `config/sonarqube/quality-gate-setup.sh`, com espera `-Dsonar.qualitygate.wait=true`.

## 2. Simulação de PR com Baixa Cobertura (Exercício)

Na branch `teste/pr-baixa-cobertura`, os testes de `NotificacaoService`, `LoginService`, `Calculadora` e `Main` foram removidos para reduzir a cobertura abaixo de 80%, mantendo o código compilável e válido no Checkstyle. O pipeline reprova automaticamente em `jacoco:check`, bloqueando a aprovação dos checks de CI.
