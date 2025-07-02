FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/ecom-0.0.1-SNAPSHOT.jar bitespeed-ecom.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "bitespeed-ecom.jar"]
