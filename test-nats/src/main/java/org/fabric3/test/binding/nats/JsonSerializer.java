package org.fabric3.test.binding.nats;

import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fabric3.api.annotation.model.Component;
import org.fabric3.api.annotation.scope.Composite;

/**
 *
 */
@Composite
@Component
public class JsonSerializer implements Function<Object, String> {
    private final ObjectMapper mapper;

    public JsonSerializer() {
        mapper = new ObjectMapper();
    }

    public String apply(Object object) {
        if (object instanceof String) {
            return (String) object;
        } else {
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new AssertionError(e);
            }
        }

    }
}
