package com.vntu.console.chat.app.configuration.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.entity.dto.StatChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatChatUserToJsonStringConverter implements Converter<StatChatUser, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convert(StatChatUser chatUser) {
        String chatUserJson = null;
        try {
            chatUserJson = objectMapper.writeValueAsString(chatUser);
        } catch (JsonProcessingException e) {
            log.error("Couldn't serialize a statChatUser object to a JSON string.", e);
        }
        return chatUserJson;
    }
}
