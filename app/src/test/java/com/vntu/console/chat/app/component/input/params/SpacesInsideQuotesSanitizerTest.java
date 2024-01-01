package com.vntu.console.chat.app.component.input.params;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class SpacesInsideQuotesSanitizerTest {

    private SpacesInsideQuotesSanitizer sanitizer;

    @Before
    public void setUp() {
        sanitizer = new SpacesInsideQuotesSanitizer('\u0000');
    }

    @Test
    public void sanitizeWith_shouldReplaceSpacesInsideQuotesToSpecialCharacter() {
        String given = "--param1=\"text inside quotes \" --param2=text --param3  param4 param5";
        String expected = "--param1=\"text\u0000inside\u0000quotes\u0000\" --param2=text --param3  param4 param5";

        String actual = sanitizer.sanitize(given);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void desanitizeWith() {
        String given = "--param1=\"text\u0000inside\u0000quotes\u0000\" --param2=text --param3  param4 param5";
        String expected = "--param1=\"text inside quotes \" --param2=text --param3  param4 param5";

        String actual = sanitizer.desanitize(given);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void sanitizeWith_shouldReplaceSpacesInsideQuotesExceptEscapedToSpecialCharacter() {
        String given = "--param1=\"text inside quotes \" --param2=text --param3=\"spaces \\\\\"e v e r y  w h e r e\\\\\"\"  param4 param5";
        String expected = "--param1=\"text\u0000inside\u0000quotes\u0000\" --param2=text --param3=\"spaces\u0000\\\\\"e\u0000v\u0000e\u0000r\u0000y\u0000\u0000w\u0000h\u0000e\u0000r\u0000e\\\\\"\"  param4 param5";

        String actual = sanitizer.sanitize(given);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void desanitizeWith_shouldReplaceSpacesInsideQuotesExceptEscapedToSpecialCharacter() {
        String given = "--param1=\"text\u0000inside\u0000quotes\u0000\" --param2=text --param3=\"spaces\u0000\\\\\"e\u0000v\u0000e\u0000r\u0000y\u0000\u0000w\u0000h\u0000e\u0000r\u0000e\\\\\"\"  param4 param5";
        String expected = "--param1=\"text inside quotes \" --param2=text --param3=\"spaces \\\\\"e v e r y  w h e r e\\\\\"\"  param4 param5";

        String actual = sanitizer.desanitize(given);

        assertThat(actual).isEqualTo(expected);
    }
}