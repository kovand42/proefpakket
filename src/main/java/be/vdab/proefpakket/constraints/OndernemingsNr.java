package be.vdab.proefpakket.constraints;

import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OndernemingsNrValidator.class)
public @interface OndernemingsNr {
    String message() default"{be.vdab.proefpakket.constraints.OndernemingsNr.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
