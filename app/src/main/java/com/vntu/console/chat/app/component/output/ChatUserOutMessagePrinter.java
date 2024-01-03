package com.vntu.console.chat.app.component.output;

import com.vntu.console.chat.app.entity.ChatUser;

public class ChatUserOutMessagePrinter {

    public void printlnMessage(String message) {
        System.out.println(message);
    }

    public void printMessage(String message) {
        System.out.print(message);
    }

    public void printfMessage(String message, Object... args) {
        System.out.printf(message, args);
    }

    public void printPrompt(ChatUser chatUser) {
        printPrompt(chatUser.getNickname(), chatUser.getId());
    }

    public void printPrompt(String username, Integer id) {
        System.out.printf("%s#%s> ",username, id);
    }

    public void printPromptMessage(ChatUser chatUser, String message) {
        System.out.printf("%s#%s> %s", chatUser.getNickname(), chatUser.getId(), message);
    }

    public void printPromptMessage(String username, Integer id, String message) {
        System.out.printf("%s#%s> %s", username, id, message);
    }
}