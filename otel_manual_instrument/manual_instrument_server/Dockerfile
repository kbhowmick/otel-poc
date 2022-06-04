FROM adoptopenjdk/openjdk11

RUN mkdir /app

COPY build/libs/*.jar /app/manual-server.jar

ENTRYPOINT ["java", "-jar", "/app/manual-server.jar"]