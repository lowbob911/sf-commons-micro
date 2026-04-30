package eu.sportsfusion.common.security.boundary.rate;

import jakarta.enterprise.util.Nonbinding;
import jakarta.ws.rs.NameBinding;
import java.lang.annotation.*;

@Inherited
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface RateLimited {
    @Nonbinding RateLimit[] value() default {};
}
