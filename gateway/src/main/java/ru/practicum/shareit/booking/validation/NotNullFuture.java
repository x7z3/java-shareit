package ru.practicum.shareit.booking.validation;

import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@ConstraintComposition
@NotNull
@Future
@Documented
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullFuture {
    String message() default "{ru.practicum.shareit.booking.validation.NotNullFuture.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
