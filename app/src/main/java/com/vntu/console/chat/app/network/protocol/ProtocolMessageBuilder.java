package com.vntu.console.chat.app.network.protocol;

import com.vntu.console.chat.app.entity.ChatUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.DISCONNECT_CHAT_USER_COMMAND;
import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.UPDATE_CHAT_USER_COMMAND;

@RequiredArgsConstructor
public class ProtocolMessageBuilder {

    private final Converter<ChatUser, String> chatUserToJsonConverter;

    public String buildUpdateChatUserCommandMessage(ChatUser chatUser) {
        return UPDATE_CHAT_USER_COMMAND + chatUserToJsonConverter.convert(chatUser);
    }

    public String buildDisconnectUserCommandMessage() {
        return DISCONNECT_CHAT_USER_COMMAND;
    }
}
