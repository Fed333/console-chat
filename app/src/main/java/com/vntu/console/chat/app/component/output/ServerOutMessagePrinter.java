package com.vntu.console.chat.app.component.output;

import com.vntu.console.chat.app.entity.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class ServerOutMessagePrinter extends MessagePrinter {

    private final PrintWriter monitoringOut;

    private final PromptMessageProvider promptMessageProvider;

    public void printPrompt() {
        System.out.print(promptMessageProvider.getServerPrompt());
    }

    public void printlnMessage(String message) {
        monitoringOut.print(promptMessageProvider.getServerPrompt());
        monitoringOut.println(message);

        consoleOutPrintln(message);
        printPrompt();
    }

    public void printfMessage(String message, Object... params) {
        monitoringOut.print(promptMessageProvider.getServerPrompt());
        monitoringOut.printf(message, params);

        consoleOutPrintf(message, params);
        printPrompt();
    }

    public void printlnMessage(ChatUser chatUser, String message) {
        monitoringOut.print(promptMessageProvider.getChatUserPrompt(chatUser));
        monitoringOut.println(message);

        consoleOutPrintln(message);
        printPrompt();
    }

    public void printlnMessage(String message, PrintWriter printWriter) {
        printWriter.print(promptMessageProvider.getServerPrompt());
        printWriter.println(message);

        consoleOutPrintln(message);
        printPrompt();
    }

    private void preDestroy()  {
        monitoringOut.close();
    }
}
