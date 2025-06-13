pipeline {
    agent any

    environment {
        DB_URL = credentials('DB_URL')           // Jenkins credential ID for DB URL
        DB_USER = credentials('DB_USER')         // Jenkins credential ID for DB user
        DB_PASSWORD = credentials('DB_PASSWORD') // Jenkins credential ID for DB password
        DOCKER_IMAGE = 'yourdockerhubusername/waste-management:latest' // Change to your Docker Hub image
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Install Dependencies') {
            agent {
                docker { image 'node:20-alpine' }
            }
            steps {
                sh 'npm install'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh "docker build -t $DOCKER_IMAGE ."
            }
        }
        stage('Push Docker Image') {
            steps {
                withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    sh 'docker login -u yourdockerhubusername -p ${dockerhubpwd}'
                    sh "docker push $DOCKER_IMAGE"
                }
            }
        }
        stage('Deploy to AWS EC2') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-key', keyFileVariable: 'EC2_KEY')]) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no -i $EC2_KEY ubuntu@13.127.235.233 "
                        docker pull subrat8908/waste-management:latest &&
                        docker stop waste-management || true &&
                        docker rm waste-management || true &&
                        docker run -d --name waste-management -p 80:3000 \
                            -e DB_URL=$DB_URL -e DB_USER=$DB_USER -e DB_PASSWORD=$DB_PASSWORD \
                            subrat8908/waste-management:latest
                    "
                    '''
                }
            }
        }
    }
} 