pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Pull the latest code from GitHub
                git branch: 'master', url: 'https://github.com/amine-bennani/my-demo-app.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Installing dependencies...'
                sh 'npm install'
            }
        }

        stage('Unit Test') {
            steps {
                echo 'Running unit tests...'
                sh 'npm test'
            }
            post {
                always {
                    // Archive the test results and coverage reports
                    junit 'coverage/**/jest-junit.xml' // or wherever you output JUnit test results
                    // You can also archive coverage HTML, e.g.:
                    // publishHTML(target: [
                    //     reportDir: 'coverage/lcov-report',
                    //     reportFiles: 'index.html',
                    //     reportName: 'Coverage Report'
                    // ])
                }
            }
        }

        stage('Check Coverage') {
            steps {
                script {
                    // A simple example of parsing coverage from the LCOV file or JSON
                    // For demonstration, let's do a naive approach:
                    def coverageFile = readFile 'coverage/coverage-summary.json'
                    def coverageJson = readJSON text: coverageFile

                    // Typically, 'lines' coverage is used as a quick check
                    def lineCoverage = coverageJson.total.lines.pct
                    echo "Line Coverage: ${lineCoverage}%"

                    if (lineCoverage < 80) {
                        error("Coverage below 80%. Failing the build.")
                    }
                }
            }
        }

        stage('Promote to UAT') {
            steps {
                echo 'Promoting build to UAT environment...'
                // Example: Copy artifacts, or run Ansible/Terraform to set up UAT environment
                // For a simple demo, you might just echo or scp to your UAT server
                sh 'echo "Deploying to UAT server..."'
            }
        }

        stage('Selenium Tests on UAT') {
            steps {
                echo 'Running Selenium tests on UAT...'
                // This could be a shell script that triggers tests using Selenium.
                // e.g. 'sh "python -m pytest --some-selenium-tests ..."'
                // For demo, just echo:
                sh 'echo "Selenium tests passed!"'
            }
        }

        stage('Deploy to GCP') {
            steps {
                echo 'Deploying to GCP...'
                // In practice, you'd run Terraform or gcloud commands here
                sh '''
                  terraform init
                  terraform plan -out=tfplan
                  terraform apply -auto-approve tfplan
                '''
            }
        }
    }
}

