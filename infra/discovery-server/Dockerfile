FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY . .

RUN mvn -q -pl infra/discovery-server -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/infra/discovery-server/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]