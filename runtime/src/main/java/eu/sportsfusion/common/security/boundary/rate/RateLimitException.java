package eu.sportsfusion.common.security.boundary.rate;

import java.io.IOException;

public class RateLimitException extends IOException {

    public RateLimitException() {
        super("Too Many Requests");
    }

}
