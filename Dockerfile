FROM openjdk:21
WORKDIR /app
COPY build/libs/BabyShop-With-ChatBot-0.0.1-SNAPSHOT.jar /BabyShop.jar
ENTRYPOINT ["java", "-jar", "/BabyShop.jar"]