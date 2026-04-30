package eu.sportsfusion.common.jsonsupport;

import jakarta.json.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

import eu.sportsfusion.common.Money;

/**
 * @author Anatoli Pranovich
 */
public class CustomJsonObjectBuilder implements ExtendedJsonObjectBuilder {

    private JsonObjectBuilder builder = Json.createObjectBuilder();

    @Override
    public CustomJsonObjectBuilder add(String name, JsonValue value) {

        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, String value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, BigInteger value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, BigDecimal value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, int value) {
        builder.add(name, value);
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, long value) {
        builder.add(name, value);
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, double value) {
        builder.add(name, value);
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, boolean value) {
        builder.add(name, value);
        return this;
    }

    @Override
    public CustomJsonObjectBuilder addNull(String name) {
        builder.addNull(name);
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, JsonObjectBuilder builder) {
        this.builder.add(name, builder);
        return this;
    }

    @Override
    public CustomJsonObjectBuilder add(String name, JsonArrayBuilder builder) {
        this.builder.add(name, builder);
        return this;
    }

    @Override
    public JsonObject build() {
        return builder.build();
    }

    public CustomJsonObjectBuilder add(String name, Instant value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value.toString());
        }
        return this;
    }

    public CustomJsonObjectBuilder add(String name, Money value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, new BigDecimal(value.toString()));
        }
        return this;
    }

    public CustomJsonObjectBuilder add(String name, LocalTime value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value.toString());
        }
        return this;
    }

    public CustomJsonObjectBuilder add(String name, Integer value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value);
        }
        return this;
    }

    public CustomJsonObjectBuilder add(String name, UUID value) {
        if (value == null) {
            builder.addNull(name);
        } else {
            builder.add(name, value.toString());
        }
        return this;
    }
}
