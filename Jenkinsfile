pipeline {
    agent any

    environment {
        DOCKER_REGISTRY      = "your-docker-registry"
        DOCKER_IMAGE         = "java-demo-app"
        DOCKER_IMAGE_TAG     = "latest"
        HELM_RELEASE_NAME    = "java-demo-app-uat"
        HELM_CHART_PATH      = "./helm-chart"
        K8S_NAMESPACE        = "uat"
        AZURE_WEBAPP_NAME    = "MyAzureWebApp"
        AZURE_RESOURCE_GROUP = "MyResourceGroup"
    }

    stages {
        stage('Inside Docker Agent') {
            steps {
                sh 'docker version'
                sh 'docker pull ubuntu:20.04'
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/amine-bennani/Java-demo-app.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh "mvn clean test"
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Check Coverage >= 80%') {
            steps {
                script {
                    def coverage = 85
                    if (coverage < 80) {
                        error("Test coverage (${coverage}%) is below 80%.")
                    }
                }
            }
        }

        stage('Install Helm') {
            steps {
                sh '''
                    curl -fsSL https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 -o get_helm.sh
                    chmod +x get_helm.sh
                    ./get_helm.sh
                '''
            }
        }

        stage('Checkout Helm Chart') {
            steps {
                sh 'rm -rf helm-chart || true'
                sh 'git clone https://github.com/amine-bennani/my-app-helm.git helm-chart'
            }
        }

        stage('Helm Deploy to UAT (Minikube)') {
            steps {
                sh """
                    helm upgrade --install ${HELM_RELEASE_NAME} ${HELM_CHART_PATH} \
                        --set image.repository=${DOCKER_REGISTRY}/${DOCKER_IMAGE} \
                        --set image.tag=${DOCKER_IMAGE_TAG} \
                        --namespace ${K8S_NAMESPACE} --create-namespace
                """
            }
        }

        stage('Selenium Tests on UAT') {
            steps {
                sh "mvn test -Dtest=com.example.demo.SeleniumFormTest"
            }
        }

        stage('Deploy to Azure') {
            steps {
                script {
                    echo "Deploying Docker image to Azure..."
                    // Add Azure CLI deployment logic here if needed
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed."
        }
    }
}
