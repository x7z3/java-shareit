package ru.practicum.shareit.booking.validation;

import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@ConstraintComposition
@NotNull
@FutureOrPresent
@Documented
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullFutureOrPresent {
    String message() default "{ru.practicum.shareit.booking.validation.NotNullFutureOrPresent.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

