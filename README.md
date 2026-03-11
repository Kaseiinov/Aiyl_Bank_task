# AIYL Bank — REST API

---

## Технологии

- Java 21
- Spring Boot 3.2.2
- PostgreSQL 16
- Liquibase
- Docker / Docker Compose

---

## Запуск

### 1. Запустить одной командой

```bash
docker compose up --build
```

Приложение запустится на `http://localhost:8080`  
База данных будет доступна на порту `5433`

---

## Подключение к БД

| Параметр | Значение     |
|----------|--------------|
| Host     | `localhost`  |
| Port     | `5433`       |
| Database | `aiylbank`   |
| User     | `aiyl`       |
| Password | `aiyl123`    |


## Swagger

После запуска документация доступна по адресу:

```
http://localhost:8080/swagger-ui/index.html
```

