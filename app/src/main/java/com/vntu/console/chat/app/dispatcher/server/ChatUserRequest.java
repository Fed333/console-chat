package com.vntu.console.chat.app.dispatcher.server;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.entity.ChatUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserRequest {

    private ChatUser chatUser;

    private ExtractedParams params;

}
