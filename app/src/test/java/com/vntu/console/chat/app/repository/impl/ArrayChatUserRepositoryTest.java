package com.vntu.console.chat.app.repository.impl;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.fixture.ChatUserFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ArrayChatUserRepositoryTest {

    @Spy
    private HashMap<Integer, ChatUser> chatUsers;

    @Spy
    private AtomicInteger sequenceId;

    @InjectMocks
    private ArrayChatUserRepository repository;

    private int sequenceIdInteger;

    @Before
    public void setUp() {
        sequenceIdInteger = 1;
        sequenceId.set(sequenceIdInteger);
    }

    @Test
    public void save_shouldStoreChatUser() {
        ChatUser chatUser = ChatUserFixture.createTestOne();

        repository.save(chatUser);

        verify(chatUsers).put(anyInt(), eq(chatUser));
    }

    @Test
    public void save_shouldGenerateSequenceId() {
        ChatUser chatUser = ChatUserFixture.createTestOne();

        repository.save(chatUser);

        verify(chatUsers).put(++sequenceIdInteger, chatUser);
    }

    @Test
    public void findAll() {
        List<ChatUser> expected = Arrays.asList(ChatUserFixture.createEmpty(), ChatUserFixture.createTestOne());
        expected.forEach(repository::save);

        List<ChatUser> actual = repository.findAll();

        assertThatList(actual).isEqualTo(expected);
    }

    @Test
    public void delete() {
        ChatUser user = ChatUserFixture.createTestOne();
        user = repository.save(user);

        repository.delete(user);

        assertNull(chatUsers.get(user.getId()));
    }
}