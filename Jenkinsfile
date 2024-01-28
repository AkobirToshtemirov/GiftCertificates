#!/usr/bin/env groovy
pipeline {
//     agent any
    agent { label 'master' }

    stages {
        stage('Clone sources') {
            steps {
                echo 'Cloning...'
                git branch: 'jenkins', url: 'https://github.com/AkobirToshtemirov/GiftCertificates'
            }
        }

        stage('Run tests') {
            steps {
                echo 'Testing...'
                bat 'mvn test'
            }
        }

        stage('Build project') {
            steps {
                echo 'Building...'
                bat 'mvn clean install'
            }
        }

        stage('Sonarqube Analysis') {
            steps {
                echo 'Sonarqube scanning...'
                bat 'mvn sonar:sonar'
            }
        }

        stage('Deploy on Tomcat') {
            steps {
                echo 'Deploying...'
                deploy adapters: [tomcat9(credentialsId: 'ab6daf10-4b8d-4c41-95ed-2e6a04e41b3c', path: '', url: 'http://localhost:8088')],
                        contextPath: '/gift-certificates-system', onFailure: false, war: '**/*.war'
            }
        }
    }
}
