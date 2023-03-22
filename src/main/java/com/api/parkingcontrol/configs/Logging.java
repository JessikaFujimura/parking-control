package com.api.parkingcontrol.configs;

import java.lang.annotation.*;
import java.util.logging.Level;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {
    Level level() default Level.NONE;

    enum Level {
        NONE, INFO, DEBUG, ERROR, WARN, TRACE
    }
}
