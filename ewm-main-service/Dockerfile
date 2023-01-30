FROM amazoncorretto:19-alpine-jdk
COPY target/*.jar service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]