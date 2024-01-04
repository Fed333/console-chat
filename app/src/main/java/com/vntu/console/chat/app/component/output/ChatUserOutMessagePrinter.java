package com.vntu.console.chat.app.component.output;

import com.vntu.console.chat.app.entity.ChatUser;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;

@RequiredArgsConstructor
public class ChatUserOutMessagePrinter {

    private final PrintWriter monitoringOut;

    private final PromptMessageProvider promptMessageProvider;

    public void printlnMessage(String message) {
        monitoringOut.println(message);
    }

    public void printMessage(String message) {
        monitoringOut.print(message);
    }

    public void printlnMessage(String message, PrintWriter printWriter) {
        printWriter.println(message);
    }

    public void printMessage(String message, PrintWriter printWriter) {
        printWriter.print(message);
    }

    public void printfMessage(String message, PrintWriter printWriter, Object... args) {
        printWriter.printf(message, args);
    }


    public void printPrompt(ChatUser chatUser) {
        printPrompt(chatUser.getNickname(), chatUser.getId());
    }

    public void printPrompt(String username, Integer id) {
        System.out.printf(promptMessageProvider.getChatUserPrompt(username, id));
    }

    public void printPromptMessage(ChatUser chatUser, String message) {
        System.out.printf("%s %s", promptMessageProvider.getChatUserPrompt(chatUser), message);
    }

    public void printPromptMessage(String username, Integer id, String message) {
        System.out.printf("%s %s", promptMessageProvider.getChatUserPrompt(username, id), message);
    }

    private void preDestroy()  {
        monitoringOut.close();
    }
}