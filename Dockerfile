FROM amazoncorretto:21-alpine

COPY target/remote-thermohygrometer-app-0.0.1-SNAPSHOT.jar /opt/remote-thermohygrometer-app/app.jar
