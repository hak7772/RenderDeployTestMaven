# Этап сборки с использованием Maven.
FROM maven:3.8.1-openjdk-8 AS builder
#  Установите рабочую директорию внутри контейнера.
WORKDIR /app
#  Скопируйте файлы pom.xml и ./src в текущую директорию.
COPY pom.xml .
COPY src ./src
# Соберите приложение.
RUN mvn clean install
RUN mvn clean package

# Этап запуска с использованием минимального образа JRE.
FROM openjdk:8-jre-slim
# Установите рабочую директорию для приложения.
WORKDIR /app
# Скопируйте jar-файл из этапа сборки.
COPY --from=builder /app/target/*.jar app.jar
# Откройте порт.
EXPOSE 8080
# Запустите приложение.
ENTRYPOINT ["java", "-jar", "app.jar"]
