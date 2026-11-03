# Atividade 2 - Pipeline CD com Jenkins

Arquivo implementado na raiz do projeto: `Jenkinsfile`.

## 1. Estágios Implementados

1. **Build:** `mvn -B clean package -DskipTests`.
2. **Test:** `mvn -B test jacoco:report` e publicação dos relatórios Surefire via `junit 'target/surefire-reports/**/*.xml'`.
3. **PMD Static Analysis (Exercício 1):** executa `mvn -B pmd:check` com as regras definidas em `config/pmd/ruleset.xml` e arquiva `target/pmd.xml`. Qualquer violação interrompe o pipeline antes do deploy.
4. **Code Style Check:** `mvn -B checkstyle:check` validando `config/checkstyle/checkstyle.xml`.
5. **Deploy to Staging (`when { branch 'main' }`):**
   - Constrói a imagem Docker versionada pelo hash do commit: `docker build -t myapp:${GIT_COMMIT} .` (usando o `Dockerfile` do projeto).
   - Aplica o manifesto Kubernetes `k8s/deployment.yaml`, atualiza a imagem da deployment `myapp` e monitora o rollout (`kubectl rollout status`).
6. **Rollback Automático em Falha no Deploy (Exercício 2):**
   - Bloco `post { failure { ... } }` acoplado diretamente ao estágio `Deploy to Staging`:
     ```groovy
     post {
         failure {
             echo "Falha detectada no Deploy to Staging. Iniciando rollback automático..."
             sh 'kubectl rollout undo deployment/myapp -n staging'
             sh 'kubectl rollout status deployment/myapp -n staging --timeout=60s'
         }
     }
     ```
7. **Notificação Global de Falha:** bloco global `post { failure { slackSend ... } }` enviando alerta ao canal `#alerts`.

## 2. Configuração do Webhook no GitHub

Para disparar o pipeline automaticamente a cada push ou Pull Request:

1. No servidor Jenkins, instale o plugin **GitHub Integration** e habilite **GitHub hook trigger for GITScm polling** no job Multibranch/Pipeline.
2. No repositório GitHub (`https://github.com/Luke2905/aula10`), acesse **Settings -> Webhooks -> Add webhook**:
   - **Payload URL:** `http://jenkins-server/github-webhook/`
   - **Content type:** `application/json`
   - **Events:** *Push events* e *Pull requests*
   - Marque **Active** e clique em **Add webhook**.
