FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml . 
RUN mvn dependency:go-offline

COPY src ./src

# Устанавливаем Chromium и зависимости для headless
RUN apt-get update && apt-get install -y \
    chromium \
    libnss3 \
    libxss1 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    libgbm1 \
    libpango-1.0-0 \
    fonts-liberation \
    libappindicator3-1 \
    xdg-utils \
 && rm -rf /var/lib/apt/lists/*

# Запуск тестов
RUN mvn clean test
