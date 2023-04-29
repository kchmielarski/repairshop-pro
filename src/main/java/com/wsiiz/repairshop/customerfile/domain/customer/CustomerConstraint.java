package com.wsiiz.repairshop.customerfile.domain.customer;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({TYPE})
@Constraint(validatedBy = CustomerConstraintValidator.class)
@Documented
public @interface CustomerConstraint {

  String message() default "";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };
}
