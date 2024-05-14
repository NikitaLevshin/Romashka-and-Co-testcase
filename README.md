# RomashkaCo
![RomashkaCo](https://i.postimg.cc/3Jr5CTDS/Frame-2.png)

Пятое задание по реализации REST-API 

Использованный стек: **Java 17, Spring-boot, PostgreSQL, Hibernate, Junit, maven, Docker**

Для запуска приложения должны быть установлены Java 17, maven, Docker

Склонировать репозиторий, затем через консоль, в директории приложения выполнить команды
```
docker-compose up
```
## Реализованные задачи
- Добавлены новые CRUD для Поставки товара, а также продажи товара
- Добавлено новое поле количество товара
- Поле "в наличии" у товара теперь меняется в зависимости от количества товара
- Нельзя продать, или поставить товар, которого не существует
- При продаже товара высчитывается стоимость покупки
- При обновлении поставки/продажи, значение количества товара будет пересчитано
- Написаны JUnit тесты на новые контроллеры и сервисы

[Документация по новому API начинается здесь](https://github.com/NikitaLevshin/Romashka-and-Co-testcase/tree/t5?tab=readme-ov-file#API-Delivery-Reference)

## API Product Reference
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
    "amount": 2
}
```
| Parameter | Description                       |
| :-------- |:-------------------------------- |
| `name`      | **Required**. Название обязательно|
| `description`| **Not-Required**. Может быть пустым|
| `price`     | **Not-Required**. Default value: 0.0|
| `isOnStock`  | **Not-Required**. Высчитывается автоматически в зависимости от количества|
| `amount` | **Not-Required**. Количество добавляемого товара|

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

## API Delivery Reference
#### Получение всех поставок
```
GET /product/delivery
```
#### Получение поставки по номеру документа
```
GET /product/delivery/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID документа для получения|

> Номер документа присваивается автоматически при создании

#### Создание поставки
```
POST /product/delivery
```
| Type     | Description                       |
 :------- | :-------------------------------- |
| `JSON` | **Required**. JSON body для отправки запроса|

Пример JSON запроса:
```
{
    "name": "Молоток",
    "amount": 5
}
```
| Parameter | Description                       |
| :-------- |:-------------------------------- |
| `name`      | **Required**. Название обязательно|
| `amount`| **Not-Required**. Количество товара, минимальное значение - 1|

> При создании доставки, товар обязательно должен быть в базе данных

#### Обновление документа по поставке
```
PATCH /product/delivery/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID документа для обновления|

| Type     | Description                       |
 :------- | :-------------------------------- |
| `JSON` | **Required**. JSON body для отправки запроса|

#### Удаление товара
```
DELETE /product/delivery/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID документа для удаления|

## API Sale Reference
#### Получение всех продаж
```
GET /product/sale
```
#### Получение продажи по номеру документа
```
GET /product/sale/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID документа для получения|

> Номер документа присваивается автоматически при создании

#### Создание продажи
```
POST /product/sale
```
| Type     | Description                       |
 :------- | :-------------------------------- |
| `JSON` | **Required**. JSON body для отправки запроса|

Пример JSON запроса:
```
{
    "name": "Молоток",
    "amount": 5
}
```
| Parameter | Description                       |
| :-------- |:-------------------------------- |
| `name`      | **Required**. Название обязательно|
| `amount`| **Not-Required**. Количество товара, минимальное значение - 1|

> При создании доставки, товар обязательно должен быть в базе данных, и его количество должно быть не меньше, чем количество продажи.
> Поле sum возвращается ответом, высчитывается автоматически

#### Обновление документа по поставке
```
PATCH /product/sale/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID документа для обновления|

| Type     | Description                       |
 :------- | :-------------------------------- |
| `JSON` | **Required**. JSON body для отправки запроса|

#### Удаление товара
```
DELETE /product/sale/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int` | **Required**. ID документа для удаления|
