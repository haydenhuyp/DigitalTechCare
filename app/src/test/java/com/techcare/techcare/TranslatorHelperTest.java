package com.techcare.techcare;
import java.io.IOException;
import static org.junit.Assert.*;

import org.junit.Test;

public class TranslatorHelperTest {
    @Test
    public void testPostTranslation() {
        TranslatorHelper translatorHelper = new TranslatorHelper();
        String originalText = "Hello, how are you?";
        try {
            String translatedResponse = translatorHelper.Post(originalText);
            assertNotNull("Translated response is null", translatedResponse);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testPrettifyJson() {
        String jsonText = "{\"data\":{\"translations\":[{\"translatedText\":\"안녕하세요. 어떻게 지내세요?\"}]}}";
        String prettifiedJson = TranslatorHelper.prettify(jsonText);
        assertNotNull("Prettified JSON is null", prettifiedJson);
        assertTrue("Prettified JSON is empty", prettifiedJson.trim().length() > 0);
    }

    @Test
    public void testExecuteTranslation() {
        String originalText = "Hello, how are you?";
        String translatedText = TranslatorHelper.execute(originalText);
        assertNotNull("Translated text is null", translatedText);
        assertFalse("Translated text is empty", translatedText.isEmpty());
    }
}
