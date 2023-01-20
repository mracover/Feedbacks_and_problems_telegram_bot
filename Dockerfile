FROM openjdk:20
ADD target/telegram_bot-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]