# OpenJDK 17 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드를 통해 생성된 JAR 파일을 컨테이너로 복사
# 현재 경로 기준으로 build/libs/ 디렉토리에 있는 *.jar 파일을 컨테이너의 /app/app.jar로 복사
COPY build/libs/*.jar app.jar

# 컨테이너 포트 8080을 외부에 노출
EXPOSE 8080

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
