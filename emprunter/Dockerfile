FROM openjdk:27-ea-oracle
VOLUME /tmp
COPY target/emprunt-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]