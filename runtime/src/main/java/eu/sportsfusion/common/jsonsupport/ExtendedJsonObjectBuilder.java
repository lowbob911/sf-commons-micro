package eu.sportsfusion.common.jsonsupport;

import jakarta.json.JsonObjectBuilder;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

import eu.sportsfusion.common.Money;

/**
 * @author Anatoli Pranovich
 */
public interface ExtendedJsonObjectBuilder extends JsonObjectBuilder {


    /**
     * Adds a name/{@code Instant} pair to the JSON object associated with
     * this object builder. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     * @throws NullPointerException if the specified name is null
     *
     * @see Instant
     */
    ExtendedJsonObjectBuilder add(String name, Instant value);

    /**
     * Adds a name/{@code Money} pair to the JSON object associated with
     * this object builder. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     * @throws NullPointerException if the specified name is null
     *
     * @see Money
     */
    ExtendedJsonObjectBuilder add(String name, Money value);

    /**
     * Adds a name/{@code LocalTime} pair to the JSON object associated with
     * this object builder. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     * @throws NullPointerException if the specified name is null
     *
     * @see LocalTime
     */
    ExtendedJsonObjectBuilder add(String name, LocalTime value);

    /**
     * Adds a name/{@code Integer} pair to the JSON object associated with
     * this object builder. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     * @throws NullPointerException if the specified name is null
     *
     * @see Integer
     */
    ExtendedJsonObjectBuilder add(String name, Integer value);

    /**
     * Adds a name/{@code UUID} pair to the JSON object associated with
     * this object builder. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     * @throws NullPointerException if the specified name is null
     *
     * @see UUID
     */
    ExtendedJsonObjectBuilder add(String name, UUID value);
}
