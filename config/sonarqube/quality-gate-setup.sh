#!/usr/bin/env bash
# Script de configuração das regras do Quality Gate no SonarQube via Web API (Atividade 3)
# Regras exigidas:
# - Cobertura mínima: 80%
# - Zero bugs críticos
# - Debt técnico < 1h (60 minutos)

set -euo pipefail

SONAR_HOST_URL="${SONAR_HOST_URL:-http://localhost:9000}"
SONAR_TOKEN="${SONAR_TOKEN:?Defina a variavel SONAR_TOKEN}"
GATE_NAME="Aula10-QualityGate"

echo "Criando Quality Gate: ${GATE_NAME}"
curl -fsS -u "${SONAR_TOKEN}:" -X POST "${SONAR_HOST_URL}/api/qualitygates/create" \
  --data-urlencode "name=${GATE_NAME}"

# 1. Cobertura mínima: 80% (falha se menor que 80%)
curl -fsS -u "${SONAR_TOKEN}:" -X POST "${SONAR_HOST_URL}/api/qualitygates/create_condition" \
  --data-urlencode "gateName=${GATE_NAME}" \
  --data-urlencode "metric=coverage" \
  --data-urlencode "op=LT" \
  --data-urlencode "error=80"

# 2. Zero bugs críticos / bloqueantes (falha se maior que 0)
curl -fsS -u "${SONAR_TOKEN}:" -X POST "${SONAR_HOST_URL}/api/qualitygates/create_condition" \
  --data-urlencode "gateName=${GATE_NAME}" \
  --data-urlencode "metric=critical_violations" \
  --data-urlencode "op=GT" \
  --data-urlencode "error=0"

# 3. Debt técnico < 1h (sqale_index em minutos; falha se maior ou igual a 60 min)
curl -fsS -u "${SONAR_TOKEN}:" -X POST "${SONAR_HOST_URL}/api/qualitygates/create_condition" \
  --data-urlencode "gateName=${GATE_NAME}" \
  --data-urlencode "metric=sqale_index" \
  --data-urlencode "op=GT" \
  --data-urlencode "error=59"

echo "Quality Gate '${GATE_NAME}' configurado com sucesso."
