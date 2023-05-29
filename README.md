# java-filmorate
Бэкенд для сервиса, который будет работать с фильмами и оценками пользователей, а также возвращать топ-5 фильмов, рекомендованных к просмотру.
---

![This is an image](scheme.png)

## Примеры запросов:
### Найти пользователя
~~~
SELECT *
FROM users
WHERE user_id = ?;
~~~
### Найти всех пользователей
~~~
SELECT *
FROM users;
~~~
### Создать пользователя
~~~
INSERT INTO users (email, login, name, birthday)
VALUES (?, ?, ?, ?);
~~~
### Обновить пользователя
~~~
UPDATE users
SET email = ?,
    login = ?,
    name = ?,
    birthday = ?
WHERE user_id = ?;
~~~
### Удалить пользователя
~~~
DELETE FROM users
WHERE user_id = ?;
~~~
