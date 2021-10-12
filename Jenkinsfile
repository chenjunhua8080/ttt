pipeline {
    agent any
    tools {
        maven 'maven3.8.3-jenkins'
    }
    stages {
       stage ('init') {
            steps {
                sh 'mvn -v'
                sh 'java -version'
            }
       }
       stage('build') {
            steps {
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
       }
       stage('find') {
            steps {
                sh 'make'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
       }
       stage('Deploy') {
           when {
             expression {
               currentBuild.result == null || currentBuild.result == 'SUCCESS'
             }
           }
           steps {
               sh 'make publish'
           }
       }
    }
 }
