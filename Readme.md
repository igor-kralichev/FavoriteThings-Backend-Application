# FavoriteThings Backend Application

## Описание

Данное приложение представляет собой бэкенд-сервис для опроса пользователей и сохранения их "любимых вещей" (Favorite Things). Пользователи могут:

- Регистрация и вход с подтверждением email через токен
- Аутентификация и авторизация с JWT (access и refresh токены)
- Создание опроса с ответами на вопросы:
  - Любимая еда (`favoriteFood`)
  - Любимый цвет в HEX формате (`favoriteColor`)
  - Любимая песня (`favoriteSong`)
  - Любимая дата (`favoriteDate`)
  - Любимое число (`favoriteNumber`)
- Просмотр собственных созданных опросов
- Поиск опросов по критериям (любимая еда, любимая песня)
- Администратор имеет роль `ROLE_ADMIN` и может получить все опросы пользователей

## Основные возможности

- **Регистрация**: сбор данных пользователя (ФИО, дата рождения, email, пароль).
- **Подтверждение email**: отправка письма со ссылкой для верификации.
- **JWT‑аутентификация**: access-токен для запросов, refresh-токен хранится в HTTP-only cookie.
- **CRUD для опросов**: создание и получение опросов через REST API.
- **Сервис отправки почты**: интеграция с SMTP (настроен для Mail.ru по умолчанию).
- **Swagger UI**: документация API доступна по `/swagger-ui.html`.

## Структура проекта

```
backend/
├── Dockerfile
├── docker-compose.yml
├── .env.properties        # Параметры окружения (игнорируется Git)
├── pom.xml
├── mvnw, mvnw.cmd, .mvn/   # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/example/favoritethings/backend/
│   │   │   ├── config/       # Конфигурация Spring, инициализация данных
│   │   │   ├── controller/   # REST-контроллеры (Auth, Survey, Admin)
│   │   │   ├── dto/          # Data Transfer Objects
│   │   │   ├── entity/       # JPA-сущности (User, Survey, Role, ...)
│   │   │   ├── repository/   # Spring Data JPA репозитории
│   │   │   ├── security/     # Конфигурация безопасности и JWT
│   │   │   └── service/      # Бизнес-логика (UserService, SurveyService, EmailService)
│   │   └── resources/
│   │       ├── application.properties  # Основные настройки (datasource, mail, swagger)
│   │       ├── static/      # Статика (пусто)
│   │       └── templates/   # Шаблоны (пусто)
│   └── test/                # Unit- и интеграционные тесты
└── target/                  # Сборка Maven (игнорируется Git)
```

## Prerequisites

- Java 21 JDK
- Maven (опционально, можно использовать `mvnw`)
- Docker & Docker Compose (для контейнеризации)
- Файл ` .env.properties` в корне проекта (детали ниже)

## Настройка `.env.properties`

Создайте в корне проекта файл `.env.properties` (он уже добавлен в `.gitignore`). Пример структуры:

```properties
# Параметры для подключения к БД PostgreSQL
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# Параметры для SMTP (Mail.ru по умолчанию)
MAIL_USERNAME=your@mail.ru
MAIL_FROM=your@mail.ru
MAIL_PASSWORD=your_smtp_password

# Необязательно: базовый URL приложения
# APP_BASE_URL=http://localhost:8090
```

## Запуск приложения

### Локально без Docker

1. Убедитесь, что PostgreSQL запущен на `localhost:5432` и создана БД `postgres`.
2. Создайте файл `.env.properties` с нужными параметрами.
3. Соберите и запустите приложение:
   ```bash
   ./mvnw clean package
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```
4. Перейдите в браузере:
   - API: `http://localhost:8090/api/...`
   - Swagger UI: `http://localhost:8090/swagger-ui.html`

### С Docker и Docker Compose

```bash
docker-compose up --build -d
```

- Сервис `backend` будет доступен на `http://localhost:8090`.
- Сервис `postgres` на порту `5432`.

## Документация API

После запуска доступна Auto-generated Swagger UI:

```
http://localhost:8090/swagger-ui.html
```

## Тестирование

Запуск unit-тестов через Maven:

```bash
./mvnw test
```



