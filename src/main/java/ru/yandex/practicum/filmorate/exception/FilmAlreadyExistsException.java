package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException(String msg) {
        super(msg);
    }

    public FilmAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FilmAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
