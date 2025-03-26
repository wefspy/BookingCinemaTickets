# 🎬 Система бронирования билетов для кинотеатра

## 📌 Описание
Система предоставляет информацию о киносеансах и позволяет бронировать билеты на фильмы.

## 🚀 Стек технологий
- **Язык**: Java 21
- **Сборщик проекта**: Maven
- **Фреймворк**: Spring Boot 3.4.3

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

## 📊 Метрики

### 🔍 Actuator
- 📌 Список всех доступных метрик:  
  [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)

- 📌 Пример конкретной метрики (максимальное число активных сессий Tomcat):  
  [http://localhost:8080/actuator/metrics/tomcat.sessions.active.max](http://localhost:8080/actuator/metrics/tomcat.sessions.active.max)
