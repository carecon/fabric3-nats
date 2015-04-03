package org.fabric3.test.binding.nats;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fabric3.api.annotation.model.Component;
import org.fabric3.api.annotation.scope.Composite;

/**
 *
 */
@Composite
@Component
public class JsonDeserializer implements Function<String, Object> {
    private final ObjectMapper mapper;

    public JsonDeserializer() {
        mapper = new ObjectMapper();
    }

    public String apply(String text) {
        try {
            return mapper.readValue(text, String.class);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
