package com.vntu.console.chat.app;

import com.vntu.console.chat.app.runner.ConsoleChatApplicationRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class ConsoleChatApplication {

    public static void main(String[] args) {
        log.info("ConsoleChatApplication context initialization...");

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        ConsoleChatApplicationRunner applicationRunner = context.getBean(ConsoleChatApplicationRunner.class);
        applicationRunner.runConsoleChat(args);

        log.info("ConsoleChatApplication has been exited!");
    }
}