# 빌드 이미지로 OpenJDK 17 & Gradle을 지정
FROM gradle:8.10.2-jdk17 AS build

# 소스 코드를 복사할 작업 디렉토리를 생성
WORKDIR /app

# 라이브러리 설치에 필요한 파일만 복사
COPY build.gradle settings.gradle ./

RUN gradle dependencies --no-daemon

# 호스트 머신의 소스코드를 작업 디렉토리로 복사
COPY . /app

# Gradle 빌드를 실행하여 JAR 파일 생성
RUN gradle clean build --no-daemon

# OpenJDK 17 기반으로 빌드
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사
COPY --from=build /app/build/libs/Sasimee-0.0.1-SNAPSHOT.jar /app/Sasimee.jar

# 포트 노출
EXPOSE 8080

# MySQL과 Redis가 준비될 때까지 대기 후 백엔드 실행
CMD ["java", "-jar", "/app/air.jar"]
