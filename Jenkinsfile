pipeline {
    agent any
    }		
    tools {
        // If using Maven, specify the installation name configured in Jenkins:
        // Add JDK if needed:
        // jdk "OpenJDK11"
    }

    environment {
        // Adjust to match your Docker registry, Helm chart, Azure settings, etc.
        DOCKER_REGISTRY      = "your-docker-registry"  // e.g., "registry.hub.docker.com"
        DOCKER_IMAGE         = "java-demo-app"
        DOCKER_IMAGE_TAG     = "latest"
        HELM_RELEASE_NAME    = "java-demo-app-uat"
        HELM_CHART_PATH      = "./helm-chart"    // Path to your Helm chart (if you have one)
        K8S_NAMESPACE        = "uat"
        AZURE_WEBAPP_NAME    = "MyAzureWebApp"
        AZURE_RESOURCE_GROUP = "MyResourceGroup"
	// If your chart is in a subfolder, adjust HELM_CHART_PATH = "helm-chart/<somefolder>"
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
                // If using a Multibranch Pipeline, Jenkins may auto-checkout. Otherwise:
                // checkout([$class: 'GitSCM', 
                //     branches: [[name: '*/main']], 
                //     userRemoteConfigs: [[url: 'https://github.com/amine-bennani/Java-demo-app.git']]
                // ])
                echo "Checking out code from repository..."
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    echo "Building and testing the Java app..."
                    // Typical Maven build/test command:
                    sh "mvn clean test"
                }
            }
            post {
                always {
                    // Archive the JUnit test results
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Check Coverage >= 80%') {
            steps {
                script {
                    echo "Checking code coverage..."
                    // ----------------------------------------------------
                    // Example if using Jacoco. Adjust for your coverage tool.
                    // By default, Jacoco writes XML report to:
                    //   target/site/jacoco/jacoco.xml
                    // You can parse that or use the Jenkins Jacoco plugin.
                    // For demonstration, let’s assume coverage is 85%:
                    // ----------------------------------------------------
                    def coverage = 85

                    if (coverage < 80) {
                        error("Test coverage (${coverage}%) is below 80%. Failing build.")
                    } else {
                        echo "Test coverage is ${coverage}%, proceeding with Docker build..."
                    }
                }
            }
        }

	stage('Install Helm') {
    	    steps {
        	sh """
          	  curl -fsSL https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 -o get_helm.sh
          	  chmod +x get_helm.sh
          	  # Force the script to skip 'sudo'
          	  ./get_helm.sh
        	"""
    	    }
	}
	stage('Helm Deploy') {
    	    steps {
                sh """
                helm version
                helm upgrade --install ...
        	"""
            }
	}
	stage('Checkout Helm Chart') {
            steps {
                script {
                    // Remove any previous clone to ensure a fresh checkout
                    sh 'rm -rf helm-chart || true'
                    
                    // Clone the external Helm repository into a local folder named 'helm-chart'
                    sh """
                       git clone https://github.com/amine-bennani/my-app-helm.git helm-chart
                    """
                }
            }
        }

        stage('Helm Deploy to UAT (Minikube)') {
            steps {
                script {
                    echo "Deploying to UAT via Helm on Minikube..."
                    // Ensure Jenkins has kubectl/helm installed & the correct kubeconfig for Minikube
                    sh """
                       helm upgrade --install ${HELM_RELEASE_NAME} ${HELM_CHART_PATH} \
                           --set image.repository=${DOCKER_REGISTRY}/${DOCKER_IMAGE} \
                           --set image.tag=${DOCKER_IMAGE_TAG} \
                           --namespace ${K8S_NAMESPACE} \
                           --create-namespace
                    """
                }
            }
        }

        stage('Selenium Tests on UAT') {
            steps {
                script {
                    echo "Running Selenium tests against UAT..."
                    // Replace with your Selenium test commands,
                    // e.g., use Maven with a Selenium profile or a separate script.
                    // This might look like:
                    // sh "mvn verify -Pselenium -Dselenium.url=http://<UAT-service-URL>"
                    // If tests fail, the build fails and won't proceed to Azure deployment.
                }
            }
        }

        stage('Deploy to Azure') {
            steps {
                script {
                    echo "Deploying Docker image to Azure..."
                    // Example using Azure CLI (must be installed on Jenkins agent).
                    // 1) Login with service principal or managed identity:
                    // sh """
                    //   az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID
                    // """
                    
                    // 2) Deploy the container to Azure Web App for Containers:
                    // sh """
                    //   az webapp create --name ${AZURE_WEBAPP_NAME} \\
                    //                    --resource-group ${AZURE_RESOURCE_GROUP} \\
                    //                    --plan MyPlan \\
                    //                    --deployment-container-image-name ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_IMAGE_TAG}
                    // """
                    
                    // Or if the Web App already exists, simply configure container:
                    // sh """
                    //   az webapp config container set --name ${AZURE_WEBAPP_NAME} \\
                    //                                  --resource-group ${AZURE_RESOURCE_GROUP} \\
                    //                                  --docker-custom-image-name ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_IMAGE_TAG} \\
                    //                                  --docker-registry-server-url https://${DOCKER_REGISTRY}
                    // """
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed. Cleaning up or sending notifications..."
            // e.g., send Slack/Email notifications
        }
    }
}
