package com.vntu.console.chat.app.dispatcher.server.command;

import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;

/**
 * Interface, designed for different console commands.
 * @author Roman_Kovalchuk
 * */
public interface ConsoleCommand {

    void process(ChatUserRequest chatUserRequest);

}