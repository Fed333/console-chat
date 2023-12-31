package com.vntu.console.chat.app.fixture;

import com.vntu.console.chat.app.entity.ChatUser;

import java.time.Instant;

public class ChatUserFixture {

    public static ChatUser createEmpty() {
        return ChatUser.builder().build();
    }

    public static ChatUser createTestOne() {
        return new ChatUser(null, "testuser", true, Instant.now());
    }
}
