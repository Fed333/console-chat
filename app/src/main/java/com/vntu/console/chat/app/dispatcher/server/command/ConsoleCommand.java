package com.vntu.console.chat.app.dispatcher.server.command;

import java.util.Map;

/**
 * Interface, designed for different console commands.
 * @author Roman_Kovalchuk
 * */
public interface ConsoleCommand {

    void process(Map<String, Object> params);

}