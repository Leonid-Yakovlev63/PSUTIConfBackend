FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app AS runner

COPY --from=builder /build/build/libs/*.jar ./app.jar

RUN chmod 755 ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
