FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /app
COPY ./src ./src
COPY pom.xml ./
RUN mvn clean package


FROM arm64v8/eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]