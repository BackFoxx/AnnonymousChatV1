package toyproject.annonymouschat.config.controller.customAnnotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnType {
    ReturnTypes type();

    public enum ReturnTypes {
        REDIRECT, FORWARD, JSON
    }
}
