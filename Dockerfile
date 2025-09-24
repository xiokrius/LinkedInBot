FROM maven:3.9.5-eclipse-temurin-17
WORKDIR /app

RUN apt-get update && apt-get install -y \
    wget unzip curl chromium chromium-driver \
    && rm -rf /var/lib/apt/lists/*

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package

CMD ["mvn", "test"]
