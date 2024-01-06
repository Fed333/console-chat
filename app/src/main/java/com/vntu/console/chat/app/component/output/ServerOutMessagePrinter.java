package com.vntu.console.chat.app.component.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class ServerOutMessagePrinter extends MessagePrinter {

    private final PrintWriter chatHistoryOut;

    private final PromptMessageProvider promptMessageProvider;

    public void printPrompt() {
        consoleOutPrint(promptMessageProvider.getServerPrompt());
    }

    public void printPrompt(PrintWriter printWriter) {
        printWriter.print(promptMessageProvider.getServerPrompt());
    }

    public void printlnPromptMessage(String message) {
        chatHistoryOut.println(message);

        consoleOutPrintln(message);
        printPrompt();
    }

    public void printfMessage(String message, Object... params) {
        chatHistoryOut.printf(message, params);

        consoleOutPrintf(message, params);
        printPrompt();
    }

    public void printlnMessage(String message, PrintWriter printWriter) {
        chatHistoryOut.println(message);

        printWriter.println(message);
    }

    public void printMessage(String message, PrintWriter chatUserWriter) {
        chatHistoryOut.print(message);

        chatUserWriter.print(message);
    }

    public void printlnPromptMessage(String message, PrintWriter printWriter) {
        chatHistoryOut.print(promptMessageProvider.getServerPrompt());
        chatHistoryOut.println(message);

        printPrompt(printWriter);
        printWriter.println(message);
    }

    private void preDestroy()  {
        chatHistoryOut.close();
    }

}