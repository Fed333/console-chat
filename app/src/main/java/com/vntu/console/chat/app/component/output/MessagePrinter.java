package com.vntu.console.chat.app.component.output;

public abstract class MessagePrinter {

    protected void consoleOutPrintln(String message) {
        cleanCurrentLine();
        System.out.println(message);
    }

    protected void consoleOutPrint(String message) {
        cleanCurrentLine();
        System.out.print(message);
    }

    protected void consoleOutPrintf(String message, Object... args) {
        cleanCurrentLine();
        System.out.printf(message, args);
    }

    private void cleanCurrentLine() {
        System.out.print("\033[1K\r");
    }

}
