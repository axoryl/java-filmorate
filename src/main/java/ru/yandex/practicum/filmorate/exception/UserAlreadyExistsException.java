package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }

    public UserAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
