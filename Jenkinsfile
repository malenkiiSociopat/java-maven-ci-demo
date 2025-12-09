pipeline {
    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        disableConcurrentBuilds()
    }
    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }
    triggers {
        pollSCM('H/2 * * * *')
    }
    stages {
        stage('Fix Git Ownership') {
            steps {
                bat 'git config --global --add safe.directory C:/repos/java-maven-ci-demo.git'
            }
        }
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: 'master']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/malenkiiSociopat/java-maven-ci-demo.git'
                    ]]
                ])
            }
        }
        stage('Build') {
            steps {
                    bat '''
                        echo "Using absolute path to Maven..."
                        "C:\\Program Files\\JetBrains\\IntelliJIdea2024.2\\plugins\\maven\\lib\\maven3\\bin\\mvn.cmd" -v
                        "C:\\Program Files\\JetBrains\\IntelliJIdea2024.2\\plugins\\maven\\lib\\maven3\\bin\\mvn.cmd" -B -U clean compile
                    '''
                }
        }
        stage('Test') {
              steps {
                bat '''
                    echo "Using absolute path to Maven..."
                    "C:\\Program Files\\JetBrains\\IntelliJIdea2024.2\\plugins\\maven\\lib\\maven3\\bin\\mvn.cmd" -B test
                '''
              }
              post {
                always {
                  junit '**/target/surefire-reports/*.xml'
                  // Публикация HTML-отчёта JaCoCo (генерируется в target/site/jacoco/index.html)
                  // Требуется плагин HTML Publibater в Jenkins.
                  
                }
              }
            }
        stage('Package') {
            steps {
                bat '''
                    echo "Using absolute path to Maven..."
                    "C:\\Program Files\\JetBrains\\IntelliJIdea2024.2\\plugins\\maven\\lib\\maven3\\bin\\mvn.cmd" -B package
                '''
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        stage('Quality gates') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'main'
                }
            }
            steps {
                echo 'Quality checks placeholder'
            }
        }
        stage('Deploy (local)') {
            when {
                branch 'main'
            }
            steps {
                bat 'java -jar target/java-maven-ci-demo-1.0.0.jar'
            }
        }
    }
    post {
        success {
            echo "Build successful for ${env.BRANCH_NAME}"
        }
        failure {
            echo "Build failed for ${env.BRANCH_NAME}"
        }
    }
}