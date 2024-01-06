package com.vntu.console.chat.app.network.protocol;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.fixture.ChatUserFixture;
import com.vntu.console.chat.app.service.ChatUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatUserFromPromptExtractorTest {

    @Mock
    private ChatUserService chatUserService;

    @InjectMocks
    private ChatUserFromPromptExtractor extractor;

    @Test
    public void extractUser_shouldFindByPromptIdAndNickname() {
        Integer id = 1;
        String nickname = "chatUser";
        ChatUser expected = ChatUserFixture.createWith(id, nickname);
        when(chatUserService.findByIdAndNickname(id, nickname))
                .thenReturn(Optional.of(expected));

        Optional<ChatUser> actual = extractor.extractUser("chatUser#1>NAME Pips");

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    public void erasePrompt_shouldRemovePromptPartFromMessage() {
        String expected = "NAME Pips";

        String actual = extractor.erasePrompt("chatUser#1>NAME Pips");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void erasePrompt_shouldReturnEmptyIfOnlyPromptGiven() {
        String expected = "";

        String actual = extractor.erasePrompt("chatUser#1>");

        assertThat(actual).isEqualTo(expected);
    }
}