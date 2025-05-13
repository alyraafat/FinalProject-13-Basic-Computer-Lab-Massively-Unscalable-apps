FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app
COPY target/*.jar /target/app.jar

EXPOSE 8080
CMD ["java", "-jar", "/target/app.jar"]