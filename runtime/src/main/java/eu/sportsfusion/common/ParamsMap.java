package eu.sportsfusion.common;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Anatoli Pranovich
 */
public class ParamsMap {

    private Map<String, String> map = new HashMap<>();

    public void put(String key, String value) {
        map.put(key, value);
    }

    public Integer getIntParam(String name) {
        return getIntParam(name, null);
    }

    public Integer getIntParam(String name, Integer defaultValue) {
        Integer intValue = null;

        try {

            if (map.get(name) != null) {
                intValue = Integer.valueOf(map.get(name));
            }

        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }

        if (intValue == null) {
            intValue = defaultValue;
        }
        return intValue;
    }

    public List<Integer> getIntParams(String name) {
        List<String> strings = getStringParams(name);

        if (strings != null) {
            try {
                return strings.stream().map(Integer::valueOf).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                throw new BadRequestException();
            }
        }
        return null;
    }

    public String getStringParam(String name) {
        return getStringParam(name, null);
    }

    public String getStringParam(String name, String defaultValue) {
        return map.getOrDefault(name, defaultValue);
    }

    public List<String> getStringParams(String name) {
        String param = map.get(name);

        if (StringUtils.isBlank(param)) {
            return null;
        }

        return Arrays.asList(param.split(","));
    }

    public Boolean getBooleanParam(String name) {
        return map.get(name) == null ? null : Boolean.valueOf(map.get(name));
    }

    public Boolean getBooleanParam(String name, Boolean defaultValue) {
        Boolean value = getBooleanParam(name);
        return value == null ? defaultValue : value;
    }

    public Instant getDateParam(String name) {
        String string = map.get(name);

        if (string != null) {
            try {
                return Instant.parse(string);
            } catch (DateTimeParseException e) {
                throw new BadRequestException();
            }
        }
        return null;
    }

    public List<Instant> getDateParams(String name) {
        List<String> strings = getStringParams(name);

        if (strings != null) {
            try {
                return strings.stream().map(Instant::parse).collect(Collectors.toList());
            } catch (DateTimeParseException e) {
                throw new BadRequestException();
            }
        }
        return null;
    }

    public boolean hasParam(String name) {
        return StringUtils.isNotBlank(map.get(name));
    }

    public UUID getUUIDParam(String name) {
        String string = map.get(name);

        if (string != null) {
            try {
                return UUID.fromString(string);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException();
            }
        }
        return null;
    }

    public List<UUID> getUUIDParams(String name) {
        List<String> strings = getStringParams(name);

        if (strings != null) {
            try {
                return strings.stream().map(UUID::fromString).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException();
            }
        }
        return null;
    }
}
