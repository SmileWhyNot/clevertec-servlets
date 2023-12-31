# Clevertec Servlets App

Этот проект представляет собой веб-приложение с использованием Java Servlets, реализующее API для управления пользователями и их ролями. Ниже представлены основные функциональности приложения:

## Функционал

1. **Управление пользователями:**
    - Создание пользователя
    - Получение информации о пользователе по ID
    - Получение информации об администраторе по ID
    - Получение списка всех пользователей
    - Обновление информации о пользователе
    - Удаление пользователя

2. **Управление ролями:**
    - Создание роли
    - Получение информации о роли по ID
    - Получение списка всех ролей
    - Обновление информации о роли
    - Удаление роли

3. **Логирование запросов:**
    - Реализовано полное логирование входящих запросов

4. **Управление доступом:**
    - Доступ к изменению ролей доступен только пользователям с ролью ADMIN
    - Изменение ролей конкретного пользователя также доступно только пользователю с ролью ADMIN

5. **Сессии:**
    - Список ролей пользователя получается из сессии
    - При изменении списка ролей пользователя во время активной сессии, список обновляется в самой сессии
    - При удалении пользователя администратором, его сессия становится невалидной

## Postman Коллекция

Для удобного тестирования API приложения предоставлена коллекция запросов Postman. Файл с коллекцией находится по пути `resources/clevertec-servlets.postman_collection.json`.

## Документация по Postman Запросам

1. **Создание пользователя:** (не требует Basic Auth)
    - Метод: `POST`
    - Endpoint: `http://localhost:8080/user`
```json
{
      "password": "qwerty",
      "username": "vlad",
      "roles": [
          {
              "id": 1,
              "roleName": "USER"
          },
          {
              "id": 2,
              "roleName": "ADMIN"
          }
      ]
}
```

### Все запросы ниже требуют аутентификации Basic Auth

2. **Получение информации о пользователе по ID:**
    - Метод: `GET`
    - Endpoint: `http://localhost:8080/user?id={userID}`

3. **Получение информации об администраторе по ID:**
    - Метод: `GET`
    - Endpoint: `http://localhost:8080/user?id={adminID}`

4. **Получение списка всех пользователей:**
    - Метод: `GET`
    - Endpoint: `http://localhost:8080/user`

5. **Обновление информации о пользователе:**
    - Метод: `PUT`
    - Endpoint: `http://localhost:8080/user`

```json
{
    "id": 3,
    "password": "qwerty",
    "username": "vlad",
    "roles": [
        {
            "id": 1,
            "roleName": "USER"
        },
        {
            "id": 2,
            "roleName": "ADMIN"
        }
    ]
}
```

6. **Удаление пользователя:**
    - Метод: `DELETE`
    - Endpoint: `http://localhost:8080/user?id={userID}`

7. **Создание роли:**
    - Метод: `POST`
    - Endpoint: `http://localhost:8080/role`

```json
{
    "roleName": "LIMITED_ADMIN"
}
```

8. **Получение информации о роли по ID:**
    - Метод: `GET`
    - Endpoint: `http://localhost:8080/role?id={roleID}`

9. **Получение списка всех ролей:**
    - Метод: `GET`
    - Endpoint: `http://localhost:8080/role`

10. **Обновление информации о роли:**
    - Метод: `PUT`
    - Endpoint: `http://localhost:8080/role`
```json
{
    "id": 3,
    "roleName": "ChangedRole"
}
```

11. **Удаление роли:**
    - Метод: `DELETE`
    - Endpoint: `http://localhost:8080/role?id={roleID}`

Для выполнения запросов необходима авторизация с использованием Basic Auth, где `Username` и `Password` соответствуют вашим учетным данным.