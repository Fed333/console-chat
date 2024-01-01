package com.vntu.console.chat.app.component.input.params;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpacesInsideQuotesSanitizer {

    private final char sanitizer;

    public String sanitize(String input) {
        char[] chars = input.toCharArray();

        boolean sanitize = false;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"' && isNotEscapedQuote(chars, i)) {
                sanitize = !sanitize;
            }
            if (chars[i] == ' ' && sanitize) {
                chars[i] = sanitizer;
            }
        }
        return new String(chars);
    }

    public String desanitize(String input) {
        return input.replace(sanitizer, ' ');
    }

    private static boolean isNotEscapedQuote(char[] chars, int i) {
        return i > 1 && chars[i - 1] != '\\' || i == 0;
    }

}
