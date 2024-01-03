package com.vntu.console.chat.app.component.output;

import com.vntu.console.chat.app.entity.ChatUser;

public class PromptMessageProvider {

    public String getServerPrompt() {
        return "server>";
    }

    public String getChatUserPrompt(String username, Integer id) {
        return String.format("%s#%s>", username, id);
    }

    public String getChatUserPrompt(ChatUser chatUser) {
        return getChatUserPrompt(chatUser.getNickname(), chatUser.getId());
    }
}
