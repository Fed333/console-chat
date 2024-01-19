package com.vntu.console.chat.app.configuration.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ObjectToJsonStringConverter implements Converter<Object, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convert(Object source) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            log.error("Couldn't serialize an object to a JSON string.", e);
        }
        return jsonString;
    }
}
