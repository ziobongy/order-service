FROM maven:3.9.14-eclipse-temurin-21 AS build

COPY . /app
WORKDIR /app
RUN mvn clean install

FROM amazoncorretto:21.0.10
COPY --from=build /app/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]