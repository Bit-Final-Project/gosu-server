pipeline {
    agent any

    stages {
        stage('Add Env') {
            steps {
                withCredentials([file(credentialsId: 'application', variable: 'APPLICATION_YML')]) {
                    sh '''
                        echo "Preparing application.yml..."
                        mkdir -p /var/jenkins_home/workspace/moeego_server/src/main/resources
                        cp $APPLICATION_YML /var/jenkins_home/workspace/moeego_server/src/main/resources/application.yml
                        echo "application.yml copied successfully."
                    '''
                }
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "Granting execution permission to gradlew..."
                    chmod +x gradlew
                    echo "Building with Gradle..."
                    ./gradlew build
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    echo "Building Docker image..."
                    docker build --build-arg JAR_FILE=build/libs/testapiserver-0.0.1-SNAPSHOT.jar -t moeego-server .
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    echo "Stopping existing Docker container..."
                    docker stop moeego-server || true
                    echo "Removing existing Docker container..."
                    docker rm moeego-server || true
                    echo "Starting new Docker container..."
                    docker run -d -p 8080:8080 --name moeego-server moeego-server
                '''
            }
        }

        stage('Run Deploy Script') {
            steps {
                sh '''
                    echo "Checking deploy.sh file..."
                    if [ ! -f ./deploy.sh ]; then
                        echo "deploy.sh not found"
                        exit 1
                    fi
                    echo "Granting execute permission to deploy.sh..."
                    chmod +x deploy.sh
                    echo "Executing deploy.sh..."
                    ./deploy.sh
                '''
            }
        }
    }
}
