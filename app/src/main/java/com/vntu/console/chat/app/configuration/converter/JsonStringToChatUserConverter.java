package com.vntu.console.chat.app.configuration.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.vntu.console.chat.app.entity.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonStringToChatUserConverter implements Converter<String, ChatUser> {

    private final ObjectMapper objectMapper;

    @Override
    public ChatUser convert(String jsonString) {
        ObjectReader objectReader = objectMapper.readerFor(ChatUser.class);
        ChatUser createdChatUser = null;
        try {
            createdChatUser = objectReader.readValue(jsonString);
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse jsonString {} to a chatUser", jsonString);
        }
        return createdChatUser;
    }
}
