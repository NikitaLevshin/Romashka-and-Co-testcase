# RomashkaCo
![RomashkaCo](https://i.postimg.cc/3Jr5CTDS/Frame-2.png)

Четвертое задание по реализации REST-API 

Использованный стек: **Java 17, Spring-boot, PostgreSQL, Hibernate, Junit, maven, Docker**

Для запуска приложения должны быть установлены Java 17, maven, Docker

Склонировать репозиторий, затем через консоль, в директории приложения выполнить команды
```
docker-compose up
```
## Реализованные задачи
- Добавлены фильтрация и сортировка для получения товаров
- Добавлена валидация сортировки
- Фильтры и сортировку можно применять вместе
- Пользователь может ограничивать количество выданных записей о товарах
- Написаны JUnit тесты на фильтрацию и сортировку

[Документация по новому API начинается здесь](https://github.com/NikitaLevshin/Romashka-and-Co-testcase/tree/t4?tab=readme-ov-file#получение-всех-товаров-используя-фильтрацию-и-сортировку)

## API Reference
#### Получение всех товаров
```
GET /product
```
#### Получение всех товаров, используя фильтрацию и сортировку
```
GET /product?name={name}&priceMore={price}&priceLess={price}&isOnStock={isOnStock}&sort={enum}&size={size}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `name`      | `String` | **Not required**. Название или часть названия для поиска|
| `priceMore`|  `double`| **Not required**. Фильтрация по цене выше заданной|
| `priceLess`| `double` | **Not required**. Фильтрация по цене ниже заданной|
| `isOnStock`| `boolean`| **Not required**. Поиск товаров в наличии/не в наличии|
| `sort`| `enum` | **Not required**. Вид сортировки, NAMEASC, NAMEDESC, PRICEASC, PRICEDESC|
| `size`| `int` | **Not required**. Количество товаров, которые будут отображены на странице|

**При применении сортировок priceMore и priceLess одновременно, фильтрация происходит от и до цены**

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
