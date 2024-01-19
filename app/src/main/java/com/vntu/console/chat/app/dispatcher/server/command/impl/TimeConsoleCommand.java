package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.entity.dto.SessionDurationMeasurementUnits;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TimeConsoleCommand implements ConsoleCommand {

    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final Converter<Object, String> objectToJsonConverter;

    @Override
    public void process(ChatUserRequest chatUserRequest) {
        Instant now = Instant.now();

        ChatUser chatUser = chatUserRequest.getChatUser();
        Instant sessionStart = chatUser.getSessionStart();

        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();

        Duration sessionDuration = Duration.between(sessionStart, now);
        long sessionDurationInSeconds = sessionDuration.getSeconds();

        SessionDurationMeasurementUnits measurementUnits = SessionDurationMeasurementUnits.builder()
                .seconds(sessionDurationInSeconds)
                .minutes(TimeUnit.SECONDS.toMinutes(sessionDurationInSeconds))
                .hours(TimeUnit.SECONDS.toHours(sessionDurationInSeconds)).build();

        String measurementUnitsJson = objectToJsonConverter.convert(measurementUnits);

        String message = String.format("Session duration of %s:\n%s", chatUser.getUsername(), measurementUnitsJson);

        serverOutMessagePrinter.printlnPromptMessage(message, chatUserWriter);
    }

    @Override
    public String getDescription() {
        return "Prints a time measurements of current session duration. Example: TIME";
    }

    @Override
    public String getSynopsis() {
        return "TIME";
    }
}