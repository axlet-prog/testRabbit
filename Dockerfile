FROM bellsoft/liberica-openjdk-debian:21
RUN apt-get update && apt-get install -y iputils-ping && apt-get install -y bash

COPY ./target/testRabbitMQ-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]