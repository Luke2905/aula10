# Atividade 2 - Pipeline CD com Jenkins

Arquivo implementado: `Jenkinsfile`.

## 1. Estrutura do `Jenkinsfile`

Baseado integralmente no `Jenkinsfile` da Aula 10, acrescido dos dois itens pedidos no exercício:

1. `stage('Build')`: `sh 'mvn clean package'`
2. `stage('Test')`: `sh 'mvn test'` e `junit 'target/surefire-reports/**/*.xml'`
3. `stage('PMD')` (Exercício 1): `sh 'mvn pmd:check'` utilizando `config/pmd/ruleset.xml`
4. `stage('Deploy to Staging')` (`when { branch 'main' }`):
   - `sh 'docker build -t myapp:${GIT_COMMIT} .'`
   - `sh 'kubectl apply -f k8s/deployment.yaml'`
   - Rollback automático em falha no deploy (Exercício 2):
     ```groovy
     post {
         failure {
             sh 'kubectl rollout undo deployment/myapp'
         }
     }
     ```
5. `post { failure { ... } }`: `slackSend channel: '#alerts', message: "Build Failed: ${env.JOB_NAME}"`

## 2. Configuração do Webhook no GitHub

Conforme indicado na Atividade 2:
- No GitHub: **Settings -> Webhooks -> Add webhook**
- **Payload URL:** `http://jenkins-server/github-webhook/`
- **Content type:** `application/json`
- **Eventos:** `push` e `pull_request`

*(Observação: não há servidor Jenkins externo provisionado neste ambiente local; o `Jenkinsfile`, o `Dockerfile` e `k8s/deployment.yaml` estão versionados na raiz conforme especificado).*
