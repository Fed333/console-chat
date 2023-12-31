package com.vntu.console.chat.app.repository.impl;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class ArrayChatUserRepository implements ChatUserRepository {

    private final Map<Integer, ChatUser> chatUsers;

    private final AtomicInteger sequenceId;

    @Override
    public List<ChatUser> findAll() {
        return new ArrayList<>(chatUsers.values());
    }

    @Override
    public ChatUser save(ChatUser user) {
        if (user.getId() == null) {
            user.setId(sequenceId.incrementAndGet());
        }
        log.info("Add new ChatUser: {}", user);
        chatUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(ChatUser user) {
        log.info("Remove ChatUser: {}", user);
        chatUsers.remove(user.getId());
    }
}