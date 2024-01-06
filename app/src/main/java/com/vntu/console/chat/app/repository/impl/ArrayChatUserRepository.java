package com.vntu.console.chat.app.repository.impl;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ArrayChatUserRepository implements ChatUserRepository {

    private final Map<Integer, ChatUser> chatUsers;

    private final AtomicInteger sequenceId;

    @Override
    public Optional<ChatUser> findByIdAndNickname(Integer id, String nickname) {
        return Optional.ofNullable(chatUsers.get(id))
                .filter(user -> Objects.equals(user.getNickname(), nickname));
    }

    @Override
    public List<ChatUser> findAll() {
        return new ArrayList<>(chatUsers.values());
    }

    @Override
    public List<ChatUser> findAllByNickname(String nickname) {
        return chatUsers.values().stream()
                .filter(chatUser -> Objects.equals(nickname, chatUser.getNickname()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatUser> findAllByLunaUser(boolean lunaUser) {
        return findAll().stream()
                .filter(chatUser -> lunaUser == chatUser.isLunaUser())
                .collect(Collectors.toList());
    }

    @Override
    public ChatUser save(ChatUser user) {
        if (user.getId() == null) {
            user.setId(sequenceId.incrementAndGet());
            log.info("Add new ChatUser: {}", user);
        }
        chatUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(ChatUser user) {
        log.info("Remove ChatUser: {}", user);
        chatUsers.remove(user.getId());
    }
}