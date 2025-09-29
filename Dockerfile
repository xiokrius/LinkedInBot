FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -DincludeScope=test

COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jdk

WORKDIR /app

# Копируем jar
COPY --from=builder /app/target/*.jar app.jar

# Устанавливаем Chromium (WebDriverManager сам подтянет chromedriver)
RUN apt-get update && apt-get install -y chromium && rm -rf /var/lib/apt/lists/*

# Указываем путь к Chromium, чтобы WebDriverManager понимал, какой браузер запускать
ENV CHROME_BIN=/usr/bin/chromium

ENTRYPOINT ["java", "-jar", "app.jar"]