# RomashkaCo
![RomashkaCo](https://i.postimg.cc/3Jr5CTDS/Frame-2.png)

Третье задание по реализации REST-API 

Использованный стек: **Java 17, Spring-boot, PostgreSQL, Hibernate, Junit, maven, Docker**

Для запуска приложения должны быть установлены Java 17, maven, Docker

Склонировать репозиторий, затем через консоль, в директории приложения выполнить команды
```
docker-compose up
```
## Реализованные задачи
- Подключен docker для запуска приложения
- dockerfile включает в себя сборку с помощью maven
- СУБД убрана в docker и запускается в отдельном контейнере

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
