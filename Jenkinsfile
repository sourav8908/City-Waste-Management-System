pipeline {
    agent any

    environment {
        DB_URL = credentials('DB_URL')           // Jenkins credential ID for DB URL
        DB_USER = credentials('DB_USER')         // Jenkins credential ID for DB user
        DB_PASSWORD = credentials('DB_PASSWORD') // Jenkins credential ID for DB password
        DOCKER_IMAGE = 'subrat8908/waste-management:latest' // Change to your Docker Hub image
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Install Dependencies') {
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
                    sh 'docker login -u subrat8908 -p ${dockerhubpwd}'
                    sh "docker push $DOCKER_IMAGE"
                }
            }
        }
        stage('Deploy to AWS EC2') {
            steps {
                echo 'Deploy step placeholder: Use SSH or AWS CLI to pull and run the Docker image on your EC2 instance.'
                // Example (uncomment and edit for real deployment):
                // sh 'ssh -o StrictHostKeyChecking=no ec2-user@your-ec2-ip "docker pull $DOCKER_IMAGE && docker run -d -p 80:3000 --env DB_URL=$DB_URL --env DB_USER=$DB_USER --env DB_PASSWORD=$DB_PASSWORD $DOCKER_IMAGE"'
            }
        }
    }
} 