package com.techcare.techcare;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.*;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import org.junit.Test;

public class YouTubeUtilityTest {
    @Test
    public void testGetServiceNotNull() {
        try {
            YouTube youTubeService = YoutubeUtility.getService();
            assertNotNull("YouTube service is null", youTubeService);
        } catch (GeneralSecurityException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetLatestVideosFromChannel() {
        try {
            SearchListResponse searchListResponse = YoutubeUtility.getLatestVideosFromChannel("UCi6JtCVy4XKu4BSG-AE2chg");
            assertNotNull("Search list response is null", searchListResponse);
            assertFalse("Search list response items are empty", searchListResponse.getItems().isEmpty());
            assertTrue("Number of items retrieved is not as expected", searchListResponse.getItems().size() <= 3);
        } catch (GeneralSecurityException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
