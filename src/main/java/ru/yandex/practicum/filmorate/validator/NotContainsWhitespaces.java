package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WhitespaceValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotContainsWhitespaces {

    String message() default "Field contains whitespaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
