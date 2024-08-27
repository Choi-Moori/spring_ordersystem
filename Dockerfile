FROM openjdk:11 as stage1
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY src src
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew
RUN ./gradlew bootJar

FROM openjdk:11 as stage2
WORKDIR /app
COPY --from=stage1 /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]