package com.vntu.console.chat.app.component.output;

public class ServerOutMessagePrinter {

    public void printPrompt() {
        System.out.print("server>");
    }

    public void printlnPromptMessage(String message) {
        printPrompt();
        System.out.println(message);
    }
}