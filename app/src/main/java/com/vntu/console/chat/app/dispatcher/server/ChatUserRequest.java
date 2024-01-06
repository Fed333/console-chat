package com.vntu.console.chat.app.dispatcher.server;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.entity.ChatUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ChatUserRequest {

    @Getter
    private ChatUser chatUser;

    private ExtractedParams params;

    @Getter
    private String commandMessage;

    public String getParameterByOrder(int index) {
        int size = params.getParamsList().size();
        if (size == 0 || size < index + 1) {
            return null;
        }

        return (String) params.getParamsList().get(index);
    }

    public String getParameterByKey(String key) {
        return (String) params.getParamsMap().get(key);
    }
}
