# RomashkaCo
![RomashkaCo](https://i.postimg.cc/3Jr5CTDS/Frame-2.png)

Второе задание по реализации REST-API 

Использованный стек: **Java 17, Spring-boot, PostgreSQL, Hibernate, Junit, maven**

Для запуска приложения должны быть установлены Java 17, maven

Склонировать репозиторий, затем через консоль, в директории приложения выполнить команды
```sh
mvn clear
mvn package
java -jar target/testcase-0.0.1-SNAPSHOT.jar
```
## Реализованные задачи
- Подключена СУБД PostgreSQL для хранения и получения товаров
- Создание таблиц происходит при запуске приложения, если не были созданы
- Данные не теряются при перезапуске приложения
- Добавлены ограничения на поля при создании таблицы
- Написаны JUnit тесты на работу репозитория и сервиса

## API Reference
#### Получение всех товаров
```
GET /product/
```
#### Получение товара по его id
```
GET /product/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID товара для получения|
#### Создание товара
```
POST /product
```
| Type     | Description                       |
 :------- | :-------------------------------- |
| `JSON` | **Required**. JSON body для отправки запроса|

Пример JSON запроса:
```
{
    "name": "Молоток",
    "description": "Хороший молоток,
    "price": 125.12,
    "isOnStock": true
}
```
| Parameter | Description                       |
| :-------- |:-------------------------------- |
| `name`      | **Required**. Название обязательно|
| `description`| **Not-Required**. Может быть пустым|
| `price`     | **Not-Required**. Default value: 0.0|
| `isOnStock`  | **Not-Required**. Default value: false|

#### Обновление товара
```
PATCH /product/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID товара для обновления|

| Type     | Description                       |
 :------- | :-------------------------------- |
| `JSON` | **Required**. JSON body для отправки запроса|

#### Удаление товара
```
DELETE /product/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID товара для удаления|
