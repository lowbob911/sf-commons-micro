package eu.sportsfusion.common.security.boundary.rate;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RateLimited.class)
@NameBinding
public @interface RateLimit {
    String name();
    RateLimitType type();
    String parameter() default "";
}
