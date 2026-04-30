package eu.sportsfusion.common.jsonapi;

import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Collection;
import java.util.UUID;

import eu.sportsfusion.common.Money;
import eu.sportsfusion.common.jsonsupport.CustomJsonObjectBuilder;
import eu.sportsfusion.common.jsonsupport.ExtendedJsonObjectBuilder;

/**
 * @author Anatoli Pranovich
 */
public class AttributesObjectBuilder implements ExtendedJsonObjectBuilder {

    private CustomJsonObjectBuilder builder = new CustomJsonObjectBuilder();
    private Collection<String> sparseFields;

    public AttributesObjectBuilder withSparseFields(Collection<String> sparseFields) {
        this.sparseFields = sparseFields;
        return this;
    }

    private boolean isSparse(String name) {
        return sparseFields == null || sparseFields.contains(name);
    }

    @Override
    public AttributesObjectBuilder add(String name, JsonValue value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, String value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, BigInteger value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, BigDecimal value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, int value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, long value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, double value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, boolean value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, Instant value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, Money value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, LocalTime value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, Integer value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, UUID value) {
        if (isSparse(name)) {
            builder.add(name, value);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder addNull(String name) {
        if (isSparse(name)) {
            builder.addNull(name);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, JsonObjectBuilder builder) {
        if (isSparse(name)) {
            this.builder.add(name, builder);
        }
        return this;
    }

    @Override
    public AttributesObjectBuilder add(String name, JsonArrayBuilder builder) {
        if (isSparse(name)) {
            this.builder.add(name, builder);
        }
        return this;
    }


    @Override
    public JsonObject build() {
        return builder.build();
    }

}
