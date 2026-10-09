
FROM maven:3.9.12-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -B -DskipTests

FROM eclipse-temurin:25-jre AS runner

WORKDIR /app

COPY --from=builder /app/target/delivery-service-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]