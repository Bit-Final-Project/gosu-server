pipeline {
    agent any
    stages {
        stage('Build') { // Gradle 빌드
            steps {
                sh './gradlew build'
            }
        }
        stage('Docker Build') { // Docker 이미지 빌드
            steps {
                sh 'docker build --build-arg JAR_FILE=build/libs/testapiserver-0.0.1-SNAPSHOT.jar -t moeego-server .'
            }
        }
        stage('Deploy') { // Docker 배포
            steps {
                sh 'docker stop moeego-server || true' // 기존 컨테이너 종료
                sh 'docker rm moeego-server || true'  // 기존 컨테이너 삭제
                sh 'docker run -d -p 8080:8080 --name moeego-server moeego-server' // 새로운 컨테이너 실행
            }
        }
        stage('Run Deploy Script') { // deploy.sh 실행 
            steps {
                sh './deploy.sh'
            }
        }
    }
}
