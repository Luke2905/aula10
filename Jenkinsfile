pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test jacoco:report'
                junit 'target/surefire-reports/**/*.xml'
            }
        }

        stage('PMD Static Analysis') {
            steps {
                sh 'mvn -B pmd:check'
                archiveArtifacts artifacts: 'target/pmd.xml', allowEmptyArchive: true
            }
        }

        stage('Code Style Check') {
            steps {
                sh 'mvn -B checkstyle:check'
            }
        }

        stage('Deploy to Staging') {
            when {
                branch 'main'
            }
            steps {
                sh 'docker build -t myapp:${GIT_COMMIT} .'
                sh 'kubectl apply -f k8s/deployment.yaml'
                sh 'kubectl set image deployment/myapp myapp=myapp:${GIT_COMMIT} -n staging'
                sh 'kubectl rollout status deployment/myapp -n staging --timeout=120s'
            }
            post {
                failure {
                    echo "Falha detectada no Deploy to Staging. Iniciando rollback automático..."
                    sh 'kubectl rollout undo deployment/myapp -n staging'
                    sh 'kubectl rollout status deployment/myapp -n staging --timeout=60s'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/site/jacoco/**/*', allowEmptyArchive: true
        }
        failure {
            slackSend channel: '#alerts', message: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
    }
}
