FROM openjdk:21-jdk-slim AS builder

WORKDIR /home/gradle/project

COPY . /home/gradle/project

RUN chmod +x /home/gradle/project/gradlew && /home/gradle/project/gradlew build -x test --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080
