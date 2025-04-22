# Этап 1: Сборка приложения
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Копируем Maven Wrapper и конфигурацию
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn ./.mvn
COPY pom.xml .

# Копируем исходный код
COPY src ./src

# Делаем mvnw исполняемым (для Linux-контейнера)
RUN chmod +x mvnw

# Собираем приложение, пропуская тесты
RUN ./mvnw clean package -DskipTests

# Этап 2: Создание финального образа
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Копируем .env.properties в контейнер
COPY .env.properties .env.properties

# Указываем часовой пояс для JVM
ENV JAVA_OPTS="-Duser.timezone=Europe/Moscow"

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]