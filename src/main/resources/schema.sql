CREATE TABLE IF NOT EXISTS users
(
    user_id  IDENTITY NOT NULL PRIMARY KEY,
    email    VARCHAR(30),
    login    VARCHAR(30),
    name     VARCHAR(30),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id IDENTITY NOT NULL PRIMARY KEY,
    name   VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS film
(
    film_id      IDENTITY NOT NULL PRIMARY KEY,
    name         VARCHAR(30),
    description  VARCHAR(200),
    release_date DATE,
    duration     INT,
    mpa_id       BIGINT DEFAULT -1,
    rate         INT    DEFAULT 0,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id) ON DELETE SET DEFAULT
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id IDENTITY NOT NULL PRIMARY KEY,
    name     VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_like
(
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendship
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status    BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE
);