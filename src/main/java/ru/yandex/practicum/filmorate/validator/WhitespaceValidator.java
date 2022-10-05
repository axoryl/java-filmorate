package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WhitespaceValidator implements ConstraintValidator<NotContainsWhitespaces, String> {

    @Override
    public void initialize(NotContainsWhitespaces constraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return !field.contains(" ");
    }
}
