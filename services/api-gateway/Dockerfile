FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
WORKDIR /home/app

COPY ./pom.xml /home/app/pom.xml
COPY ./src/main/java/com/moldavets/microservices/api_gateway/ApiGatewayApplication.java /home/app/src/main/java/com/moldavets/microservices/api_gateway/ApiGatewayApplication.java

RUN mvn -f /home/app/pom.xml clean package -DskipTests

COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-alpine
EXPOSE 8765
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]