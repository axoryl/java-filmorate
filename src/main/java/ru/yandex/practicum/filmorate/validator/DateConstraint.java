package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConstraint {

    String message() default "The date is not in the specified range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String min();
    String max() default "+999999999-12-31";
}
