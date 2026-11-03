# Atividade 3 - Automação de Qualidade e Bloqueio de PRs

Arquivos relacionados:
- `.github/workflows/ci.yml` (job `sonarqube` / `Quality Gate`)
- `sonar-project.properties`
- `config/sonarqube/quality-gate-setup.sh`

## 1. Regras do Quality Gate

Conforme especificado na Atividade 3:
- **Cobertura mínima:** `80%` (validada tanto localmente via `jacoco:check` quanto no SonarQube com `metric=coverage < 80`).
- **Zero bugs críticos:** validado por PMD/Checkstyle sem violações e condição `critical_violations > 0` no SonarQube.
- **Debt técnico < 1h:** condição `sqale_index > 59` (em minutos) configurada via `config/sonarqube/quality-gate-setup.sh`, com espera síncrona `-Dsonar.qualitygate.wait=true`.

## 2. Simulação de PR com Baixa Cobertura (Exercício)

Para demonstrar o bloqueio real de um Pull Request com cobertura insuficiente:

1. Foi criada a branch `teste/pr-baixa-cobertura` adicionando a classe `RelatorioAuditoriaFormatter` sem testes correspondentes.
2. Nessa branch, a cobertura de linhas e branches cai abaixo do limite mínimo de `80%` (`0.80`).
3. Ao abrir o Pull Request para `main`, o job `Quality Gate` (`sonarqube`) e o job `build-and-test` reprovam automaticamente na etapa `jacoco:check`, bloqueando a integração do código defeituoso.
4. Os links, SHAs e logs reais da rejeição no GitHub Actions estão documentados em `evidencias/VALIDACAO.md` e `evidencias/github-validacao.json`.
