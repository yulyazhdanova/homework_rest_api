pipeline {
    agent {
        docker {
            image 'maven:3.9.11-eclipse-temurin-17'
            args '-u root'
        }
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/yulyazhdanova/autotests_web_patterns.git'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }

    post {
        always {
            allure([
            //Генерация Allure отчета
                includeProperties: false,
                jdk: '',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}