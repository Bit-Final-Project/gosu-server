pipeline {
    agent any

    stages {
        stage('Add Env') {
            steps {
                withCredentials([file(credentialsId: 'application', variable: 'APPLICATION_YML')]) {
                    // application.yml 파일 복사
                    sh 'cp $APPLICATION_YML /var/jenkins_home/workspace/moeego_server/src/main/resources/application.yml'
                }
            }
        }

        stage('Build') { // Gradle 빌드
            steps {
                sh 'chmod +x gradlew' // gradlew에 실행 권한 추가
                sh './gradlew build'   // Gradle 빌드
            }
        }

        stage('Docker Build') { // Docker 이미지 빌드
            steps {
                sh 'docker build --build-arg JAR_FILE=build/libs/testapiserver-0.0.1-SNAPSHOT.jar -t moeego-server .' // Docker 이미지 빌드
            }
        }

        stage('Deploy') { // Docker 배포
            steps {
                sh 'docker stop moeego-server || true'  // 기존 컨테이너 종료
                sh 'docker rm moeego-server || true'   // 기존 컨테이너 삭제
                sh 'docker run -d -p 8080:8080 --name moeego-server moeego-server' // 새로운 컨테이너 실행
            }
        }

        stage('Run Deploy Script') { // deploy.sh 실행
            steps {
                sh 'chmod +x deploy.sh' // deploy.sh에 실행 권한 추가
                sh './deploy.sh'        // deploy.sh 실행
            }
        }
    }
}
