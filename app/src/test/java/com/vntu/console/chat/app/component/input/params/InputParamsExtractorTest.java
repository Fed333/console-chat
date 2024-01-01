package com.vntu.console.chat.app.component.input.params;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;


@RunWith(MockitoJUnitRunner.class)
public class InputParamsExtractorTest {

    @InjectMocks
    private InputParamsExtractor extractor;


    @Test
    public void extractCommand_shouldExtractToFirstSpace() {
        String expected = "command";

        String actual = extractor.extractCommand("command  --param1 --param2 --param3");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void extractParams_shouldExtractFlags() {
        Map<String, Object> expected = Map.of(
                "--param1", "--param1",
                "--param2", "--param2",
                "--param3", "--param3"
        );

        Map<String, Object> actual = extractor.extractParams("command  --param1 --param2 --param3").getParamsMap();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void extractParams_shouldExtractFlagParameters() {
        Map<String, Object> expected = Map.of(
                "--param1", "param1",
                "--param2", "param2",
                "--param3", "param3"
        );

        Map<String, Object> actual = extractor.extractParams("command  --param1=param1 --param2=param2 --param3=param3").getParamsMap();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void extractParams_shouldExtractNoFlagParameters() {
        List<Object> expected = Arrays.asList("param1", "param2");

        List<Object> actual = extractor.extractParams("command  param1 param2").getParamsList();

        assertThatList(actual).isEqualTo(expected);
    }

    @Test
    public void extractParams_shouldTrimDoubleQuotesForFlagParameters() {
        Map<String, Object> expected = Map.of(
                "--param1", "param1",
                "--param2", "param2"
        );

        Map<String, Object> actual = extractor.extractParams("command  --param1=\"param1\" --param2=param2").getParamsMap();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void extractParams_shouldNotSplitSpacesInDoubleQuotesForFlagParameters() {
        Map<String, Object> expected = Map.of(
                "--param1", "param1 with spaces",
                "--param2", "param2"
        );

        Map<String, Object> actual = extractor.extractParams("command  --param1=\"param1 with spaces\" --param2=\"param2\"").getParamsMap();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void extractParams_shouldNotSplitSpacesInDoubleQuotesForNoFlagParameters() {
        List<Object> expected = Arrays.asList("param1 with spaces", "param2");

        List<Object> actual = extractor.extractParams("command  \"param1 with spaces\" param2").getParamsList();

        assertThatList(actual).isEqualTo(expected);
    }

    @Test
    public void extractParams_shouldNotSplitSpacesInDoubleQuotesWithEscapedQuotesInsideForNoFlagParameters() {
        List<Object> expected = Arrays.asList("param1 with spaces and \\\\\"escaped quotes\\\\\"", "param2");

        List<Object> actual = extractor.extractParams("command  \"param1 with spaces and \\\\\"escaped quotes\\\\\"\" param2").getParamsList();

        assertThatList(actual).isEqualTo(expected);
    }
}