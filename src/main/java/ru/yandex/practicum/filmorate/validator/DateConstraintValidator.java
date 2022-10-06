package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateConstraintValidator implements ConstraintValidator<DateConstraint, LocalDate> {

    private LocalDate annotationMin;
    private LocalDate annotationMax;

    @Override
    public void initialize(DateConstraint constraint) {
        annotationMin = LocalDate.parse(constraint.min());
        annotationMax = LocalDate.parse(constraint.max());
    }

    @Override
    public boolean isValid(LocalDate field, ConstraintValidatorContext cxt) {
        return  field.isEqual(annotationMin)
                || (field.isAfter(annotationMin) && field.isBefore(annotationMax));

    }
}
