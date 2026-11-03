## Descrição das Alterações

Descreva resumidamente o objetivo deste Pull Request.

## Checklist de Automação de Qualidade (Aula 10)

- [ ] Build Maven (`mvn clean package`) aprovado em JDK 17
- [ ] Testes unitários JUnit executados sem falhas
- [ ] Cobertura JaCoCo acima do limite mínimo (80% no CI / > 85% no Desafio Integrado)
- [ ] Sem violações no Checkstyle (`mvn checkstyle:check`) e PMD (`mvn pmd:check`)
- [ ] Quality Gate (SonarQube / validação estática) aprovado
- [ ] Testes de integração com TestContainers (`-Pintegration-tests`) aprovados
- [ ] Sem vulnerabilidades reportadas pelo OWASP Dependency-Check
