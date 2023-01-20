FROM openjdk:17
EXPOSE 9090

ADD build/libs/diplom-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]