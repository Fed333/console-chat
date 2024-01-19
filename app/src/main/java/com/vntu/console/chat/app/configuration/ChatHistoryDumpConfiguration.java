package com.vntu.console.chat.app.configuration;


import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.component.output.PromptMessageProvider;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Configuration
public class ChatHistoryDumpConfiguration {

    private static final String CHAT_HISTORY_FOLDER = "chat-history";

    @Bean
    public PromptMessageProvider promptMessageProvider() {
        return new PromptMessageProvider();
    }

    @Bean(destroyMethod = "preDestroy")
    public ServerOutMessagePrinter serverOutMessagePrinter() {
        return new ServerOutMessagePrinter(openServerPrintWriter(), promptMessageProvider());
    }

    @Bean(destroyMethod = "preDestroy")
    public ChatUserOutMessagePrinter chatUserOutMessagePrinter() {
        return new ChatUserOutMessagePrinter(openClientPrintWriter(), promptMessageProvider());
    }

    private PrintWriter openServerPrintWriter() {
        log.info("Opening server print writer...");
        try {
            createChatHistoryFolderIfMissing();
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(CHAT_HISTORY_FOLDER + "/server.txt", true), true);
            log.info("Server print writer was opened successfully.");
            return printWriter;

        } catch (IOException e) {
            log.error("Couldn't open file writer to monitoring/server.txt");
            throw new RuntimeException(e);
        }
    }

    private PrintWriter openClientPrintWriter() {
        log.info("Opening client print writer...");
        try {
            createChatHistoryFolderIfMissing();
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(CHAT_HISTORY_FOLDER + "/clients.txt", true), true);
            log.info("Client print writer was opened successfully.");
            return printWriter;

        } catch (IOException e) {
            log.error("Couldn't open file writer to monitoring/server.txt");
            throw new RuntimeException(e);
        }
    }

    private void createChatHistoryFolderIfMissing() {
        File file = new File(CHAT_HISTORY_FOLDER);
        if (!file.exists()) {
            boolean chatHistoryDir = file.mkdirs();
            if (!chatHistoryDir) {
                throw new IllegalStateException("Could not create required directory: " + file.getAbsolutePath());
            }
        } else if (!file.isDirectory()) {
            throw new IllegalStateException("A file exists with the required directory name: " + file.getAbsolutePath());
        }
    }

}