#!/bin/bash

# 1. 환경 설정
PROJECT_DIR="/var/jenkins_home/workspace/backend"    # 실제 Spring Boot 프로젝트 경로
YML_SOURCE="/var/jenkins_home/workspace/backend/src/main/resources/application.yml" # .yml 파일의 실제 경로
YML_TARGET="$PROJECT_DIR/src/main/resources/application.yml"  # 서버에서의 위치
JAR_FILE="moeego-server.jar"     # 빌드된 JAR 파일 이름

# 2. .yml 파일 복사 (파일이 없으면 복사)
echo "Checking if application.yml exists..."
if [ ! -f "$YML_TARGET" ]; then
  echo "application.yml not found, copying from source..."
  if [ -f "$YML_SOURCE" ]; then
    cp "$YML_SOURCE" "$YML_TARGET"
    echo "application.yml copied successfully."
  else
    echo "Error: application.yml not found at $YML_SOURCE"
    exit 1
  fi
else
  echo "application.yml already exists."
fi

# 3. 빌드
echo "Building the project..."
cd "$PROJECT_DIR"
./gradlew clean build || { echo "Build failed"; exit 1; }

# 4. Docker 빌드
echo "Building Docker image..."
docker build -t moeego-server . || { echo "Docker build failed"; exit 1; }

# 5. 기존 컨테이너 종료
echo "Stopping existing container..."
docker stop moeego-server || echo "No existing container to stop"
docker rm moeego-server || echo "No existing container to remove"

# 6. 새 컨테이너 실행
echo "Starting new container..."
docker run -d --name moeego-server -p 8080:8080 moeego-server || { echo "Docker run failed"; exit 1; }

echo "Deployment completed successfully."
