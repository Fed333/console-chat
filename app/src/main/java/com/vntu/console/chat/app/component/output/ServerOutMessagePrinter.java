package com.vntu.console.chat.app.component.output;

import com.vntu.console.chat.app.entity.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class ServerOutMessagePrinter {

    private final PrintWriter monitoringOut;

    private final PromptMessageProvider promptMessageProvider;

    public void printPrompt() {
        System.out.print(promptMessageProvider.getServerPrompt());
    }

    public void printlnMessage(String message) {
        monitoringOut.print(promptMessageProvider.getServerPrompt());
        monitoringOut.println(message);
    }

    public void printlnMessage(ChatUser chatUser, String message) {
        monitoringOut.print(promptMessageProvider.getChatUserPrompt(chatUser));
        monitoringOut.println(message);
    }

    public void printlnMessage(String message, PrintWriter printWriter) {
        printWriter.print(promptMessageProvider.getServerPrompt());
        printWriter.println(message);
    }

    private void preDestroy()  {
        monitoringOut.close();
    }
}
