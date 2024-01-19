package com.vntu.console.chat.app.network.protocol;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ChatUserFromPromptExtractor {

    private final ChatUserService chatUserService;

    public Optional<ChatUser> extractUser(String promptMessage) {
        String chatUserOrigin = promptMessage.substring(0, promptMessage.indexOf(">"));
        String[] chatUserPromptParams = chatUserOrigin.split("#");

        String nickname = chatUserPromptParams[0];
        Integer id = Integer.parseInt(chatUserPromptParams[1]);

        return chatUserService.findByIdAndNickname(id, nickname);
    }

    public String erasePrompt(String promptMessage) {
        return promptMessage.substring(promptMessage.indexOf(">") + 1);
    }
}
