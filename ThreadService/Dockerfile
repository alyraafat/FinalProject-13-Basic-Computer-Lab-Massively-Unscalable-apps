FROM  openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
COPY ./target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]