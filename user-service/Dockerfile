FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

COPY target/*.jar /app/app.jar
COPY target/ target/

CMD ["java", "-jar", "app.jar"]