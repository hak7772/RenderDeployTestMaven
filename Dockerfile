# Базовый образ: выбираем openjdk:8-jdk-alpine для минимального размера.
# Alpine Linux - дистрибутив Linux, известный своим небольшим размером.
FROM openjdk:8-jdk-alpine

# Метка для указания информации о сборке (опционально, но полезно).
LABEL maintainer="your.email@example.com"

# Создаем рабочую директорию внутри контейнера для нашего приложения.
WORKDIR /app

# Копируем JAR-файл Spring Boot приложения в контейнер.
# Предполагается, что Maven/Gradle собирает JAR в target/.
# Убедитесь, что путь соответствует вашей структуре проекта.
COPY --from=builder /target/*.jar application.jar

# Открываем порт 8080 для доступа к приложению извне.  Уточните, какой
# порт использует ваше приложение (может быть и другой).
EXPOSE 8080

# Определяем, что произойдет при запуске контейнера. Запускаем java с 
# параметром -jar для запуска Spring Boot приложения.  Опции JVM можно
# добавить здесь.  Например: -Xmx256m для ограничения памяти.
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "application.jar"]

# Важные замечания:
# -  При сборке Docker image, убедитесь, что build context указывает на
#    директорию с Dockerfile. Используйте: docker build -t <image_name> .
# -  Проверяйте логи приложения внутри контейнера после запуска для
#    диагностики ошибок. Docker logs <container_id>.
# -  Если используются профили Spring, их нужно указывать при запуске:
#    java -Dspring.profiles.active=production -jar application.jar
