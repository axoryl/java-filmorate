# java-filmorate

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