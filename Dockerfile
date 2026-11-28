FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

COPY . .

RUN mvn package -DskipTests

CMD ["java", "-jar", "target/edis-pisos-1.0.0-SNAPSHOT.jar"]
