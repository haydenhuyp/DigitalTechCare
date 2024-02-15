/* Reference: https://www.youtube.com/watch?v=DuudSp4sHmg&ab_channel=ShubhamGautam */
/**
 * This class is to search for the latest video from the channel
 */
package com.techcare.techcare;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
        import com.google.api.client.json.jackson2.JacksonFactory;
        import com.google.api.services.youtube.YouTube;
        import com.google.api.services.youtube.model.SearchListResponse;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.security.GeneralSecurityException;
        import java.util.Collections;

public class YoutubeUtility {
    private static final String APPLICATION_NAME = "Digital Tech Care";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException
     * @return SearchListResponse
     */
    public static SearchListResponse getLatestVideosFromChannel(String channelId)
            throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list(Collections.singletonList("snippet"));
        SearchListResponse response = request.setChannelId(channelId)
                .setKey(DataUtility.YOUTUBE_API_KEY)
                .setMaxResults(3L)
                .setOrder("date")
                .execute();
        return response;
    }
}
