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
        consoleOutPrint(promptMessageProvider.getServerPrompt());
    }

    public void printPrompt(PrintWriter printWriter) {
        printWriter.print(promptMessageProvider.getServerPrompt());
    }

    public void printlnPromptMessage(String message) {
        monitoringOut.println(message);

        consoleOutPrintln(message);
        printPrompt();
    }

    public void printfMessage(String message, Object... params) {
        monitoringOut.printf(message, params);

        consoleOutPrintf(message, params);
        printPrompt();
    }

    public void printlnMessage(String message, PrintWriter printWriter) {
        monitoringOut.println(message);

        printWriter.println(message);
    }

    public void printMessage(String message, PrintWriter chatUserWriter) {
        monitoringOut.print(message);

        chatUserWriter.print(message);
    }

    public void printlnPromptMessage(String message, PrintWriter printWriter) {
        monitoringOut.print(promptMessageProvider.getServerPrompt());
        monitoringOut.println(message);

        printPrompt(printWriter);
        printWriter.println(message);
    }

    private void preDestroy()  {
        monitoringOut.close();
    }

}