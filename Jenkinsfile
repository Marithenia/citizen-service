pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = 'marithenia/citizen-service'
        DOCKER_IMAGE_TAG = "${env.BUILD_NUMBER}"
        ANSIBLE_REPO_URL = 'https://github.com/Marithenia/pension-app-ansible.git'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Marithenia/citizen-service.git'
            }
        }

        stage('Test') {
            steps {
                sh 'docker run --rm -v "$PWD":/app -w /app maven:3.9-eclipse-temurin-21 mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -t ${DOCKER_IMAGE_NAME}:latest ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh 'echo "$DOCKERHUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin'
                    sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                    sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy with Ansible') {
            steps {
                dir('ansible') {
                    git branch: 'main', url: "${ANSIBLE_REPO_URL}"
                    ansiblePlaybook(playbook: 'deploy-docker.yml', inventory: 'hosts.yaml', credentialsId: 'azure-ssh')
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
    }
}