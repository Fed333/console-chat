package com.vntu.console.chat.app.component.output;

import com.vntu.console.chat.app.entity.ChatUser;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;

@RequiredArgsConstructor
public class ChatUserOutMessagePrinter extends MessagePrinter {

    private final PrintWriter monitoringOut;

    private final PromptMessageProvider promptMessageProvider;

    public void printlnMessage(String message) {
        monitoringOut.println(message);

        consoleOutPrintln(message);
    }

    public void printMessage(String message) {
        monitoringOut.print(message);

        consoleOutPrint(message);
    }

    public void printlnMessage(String message, PrintWriter printWriter) {
        monitoringOut.println(message);

        printWriter.println(message);
    }

    public void printMessage(String message, PrintWriter printWriter) {
        monitoringOut.print(message);

        printWriter.print(message);
    }

    public void printfMessage(String message, PrintWriter printWriter, Object... args) {
        monitoringOut.printf(message, args);

        printWriter.printf(message, args);
    }

    public void printPrompt(ChatUser chatUser) {
        printPrompt(chatUser.getNickname(), chatUser.getId());
    }

    public void printPrompt(String username, Integer id) {
        consoleOutPrint(promptMessageProvider.getChatUserPrompt(username, id));
    }

    public void printPrompt(ChatUser chatUser, PrintWriter out) {
        out.print(promptMessageProvider.getChatUserPrompt(chatUser));
    }

    public void printPromptMessage(ChatUser chatUser, String message) {
        consoleOutPrintf("%s %s", promptMessageProvider.getChatUserPrompt(chatUser), message);
    }

    public void printPromptMessage(String username, Integer id, String message) {
        consoleOutPrintf("%s %s", promptMessageProvider.getChatUserPrompt(username, id), message);
    }

    private void preDestroy()  {
        monitoringOut.close();
    }
}