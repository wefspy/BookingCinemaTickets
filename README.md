# 🎬 Система бронирования билетов для кинотеатра

## 📌 Описание

Система предоставляет информацию о киносеансах и позволяет бронировать билеты на фильмы.

## 🚀 Стек технологий

- **Язык**: Java 21
- **Сборщик проекта**: Maven
- **Фреймворк**: Spring Boot 3.4.3

## 🛠️ Подготовка к запуску

Перед запуском приложения создайте файл `.env` в корневой директории. В качестве примера используйте `.env.example`.

### **Шаги:**

1. **Скопируйте** файл `.env.example`:
   ```sh
   cp .env.example .env
   ```
2. Заполните `.env` своими данными

## 📡 Запуск проекта

### 🔧 Сборка

```sh
mvn clean install
````

### 🚀 Запуск приложения

```sh
mvn spring-boot:run
```

После успешного развертывания приложение будет доступно по адресу:

🔗 [http://localhost:8080/](http://localhost:8080/)

## 📐 ER-диаграмма базы данных

Для лучшего понимания структуры базы данных и связей между таблицами представлена **ER-диаграмма**:

🔗 [dbdiagram.io](https://dbdiagram.io/d/bookingCinemaTickets-6719f49497a66db9a3194758)

## 📊 Метрики

### 🔍 Actuator

- 📌 Список всех доступных метрик:  
  [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)

- 📌 Пример конкретной метрики (максимальное число активных сессий Tomcat):  
  [http://localhost:8080/actuator/metrics/tomcat.sessions.active.max](http://localhost:8080/actuator/metrics/tomcat.sessions.active.max)

## 📘 Swagger-документация

Для просмотра и тестирования REST API доступен интерфейс Swagger UI:

🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)