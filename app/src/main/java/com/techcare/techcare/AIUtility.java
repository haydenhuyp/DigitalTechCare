package com.techcare.techcare;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AIUtility {
    /*
     * This class is for using cloud AI services.
     */
    private static String key = "";

    // Instantiates the OkHttpClient.
    OkHttpClient client = new OkHttpClient();

    // This function performs a POST request.
    public String Post() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\n" +
                        "  \"model\": \"gpt-3.5-turbo\",\n" +
                        "    \"messages\": [\n" +
                        "      {\n" +
                        "        \"role\": \"system\",\n" +
                        "        \"content\": \"You are a helpful assistant.\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"role\": \"user\",\n" +
                        "        \"content\": \"Say this is a test\"\n" +
                        "      }\n" +
                        "    ]\n" +
                        "}");
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + key)
                // location required if you're using a multi-service or regional (not global) resource.
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // This function prettifies the json response.
    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    public static void execute(){
        try {
            AIUtility request = new AIUtility();
            String response = request.Post();
            APIResponse apiResponse = new Gson().fromJson(response, APIResponse.class);
            apiResponse.jsonString = prettify(response);
            Log.d("GPTAPI", apiResponse.jsonString);
        } catch (Exception e) {
            Log.w("GPTAPI", "Error: " + e.getMessage());
        }
    }

    private class APIResponse{
        private String id;
        private String object;
        private int created;
        private String model;
        private Choice[] choices;
        public String jsonString;

        private class Choice{
            private int index;
            private Message message;
            private String finish_reason;

            private class Message{
                private String role;
                private String content;
            }
        }
        private Usage usage;
        private class Usage{
            private int prompt_tokens;
            private int completion_tokens;
            private int total_tokens;
        }

    }
    /*
    Sample Response: 
    {
      "id": "chatcmpl-8dfXK4lWFGDxNewhP8wuL0ZIPk3FJ",
      "object": "chat.completion",
      "created": 1704465150,
      "model": "gpt-3.5-turbo-0613",
      "choices": [
        {
          "index": 0,
          "message": {
            "role": "assistant",
            "content": "Sure, this is a test. How can I assist you with it?"
          },
          "finish_reason": "stop"
        }
      ],
      "usage": {
        "prompt_tokens": 22,
        "completion_tokens": 15,
        "total_tokens": 37
      }
    }
    */
}
