package com.techcare.techcare;

public class YoutubeVideo {
    private String videoUrl;
    private String videoTitle;

    public YoutubeVideo(String videoId, String videoTitle) {
        this.videoUrl = "https://www.youtube.com/watch?v=" + videoId;
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}
