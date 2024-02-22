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

public class VertexAIHelper {

    // Instantiates the OkHttpClient.
    OkHttpClient client = new OkHttpClient();

    // This function performs a POST request.
    public String Post(String prompt) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        prompt = "Translate this message to Korean: " + prompt;
        String instance =
                "{ \"prompt\": " + "\"" + prompt + "\"}";
        String parameters =
                "{\n"
                        + "  \"temperature\": 0.2,\n"
                        + "  \"maxOutputTokens\": 256,\n"
                        + "  \"topP\": 0.95,\n"
                        + "  \"topK\": 40\n"
                        + "}";
        RequestBody body = RequestBody.create(mediaType,
                "{\n"
                        + "  \"instances\": [\n"
                        + instance
                        + "  ],\n"
                        + "  \"parameters\": " + parameters + "\n"
                        + "}");
        Request request = new Request.Builder()
                .url("https://us-central1-aiplatform.googleapis.com/v1/projects/digitaltechcare-26d8a/locations/us-central1/publishers/google/models/text-bison:predict")
                .post(body)
                .addHeader("Authorization", "Bearer " + DataUtility.GCP_ACCESS_TOKEN)
                .addHeader("Content-type", "application/json; charset=utf-8")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String execute(String prompt) {
        try {
            VertexAIHelper request = new VertexAIHelper();
            String response = request.Post(prompt);
            /* Parse the response to VertexAIResponse*/
            Gson gson = new Gson();
            VertexAIResponse vertexAIResponse = gson.fromJson(response, VertexAIResponse.class);
            /* Return the content of the response if it is safe */
            return isResponseSafe(vertexAIResponse) ? vertexAIResponse.predictions[0].content : "";
        } catch (Exception e) {
            Log.w("Vertex AI", "Error: " + e.getMessage());
            return "";
        }
    }

    public static boolean isResponseSafe(VertexAIResponse vertexAIResponse){
        /* Check if Dangerous Content, Harassment, Hate Speech, or Sexually Explicit is not NEGLIGIBLE */
        for (VertexAIResponse.Prediction.SafetyAttributes.SafetyRating safetyRating : vertexAIResponse.predictions[0].safetyAttributes.safetyRatings) {
            if (!safetyRating.severity.equals("NEGLIGIBLE")) {
                return false;
            }
        }
        return true;
    }

    private class VertexAIResponse {
        public Prediction[] predictions;

        private class Prediction {
            public CitationMetadata citationMetadata;
            public String content;
            public SafetyAttributes safetyAttributes;

            private class CitationMetadata {
                public Citation[] citations;

                private class Citation {
                }
            }

            private class SafetyAttributes {
                public String[] categories;
                public float[] scores;
                public SafetyRating[] safetyRatings;
                public boolean blocked;

                private class SafetyRating {
                    public float severityScore;
                    public float probabilityScore;
                    public String category;
                    public String severity;
                }
            }
        }
    }
    /* Sample response
    Response: {
          "predictions": [
            {
              "citationMetadata": {
                "citations": []
              },
              "content": " This is a test message to ensure that your printer is working properly. If you are able to read this message, then your printer is working correctly.",
              "safetyAttributes": {
                "categories": [
                  "Derogatory",
                  "Health",
                  "Insult",
                  "Religion & Belief"
                ],
                "scores": [
                  0.1,
                  0.1,
                  0.1,
                  0.3
                ],
                "safetyRatings": [
                  {
                    "severityScore": 0.1,
                    "probabilityScore": 0.1,
                    "category": "Dangerous Content",
                    "severity": "NEGLIGIBLE"
                  },
                  {
                    "category": "Harassment",
                    "severityScore": 0,
                    "probabilityScore": 0.1,
                    "severity": "NEGLIGIBLE"
                  },
                  {
                    "severity": "NEGLIGIBLE",
                    "category": "Hate Speech",
                    "severityScore": 0.1,
                    "probabilityScore": 0.1
                  },
                  {
                    "probabilityScore": 0,
                    "severity": "NEGLIGIBLE",
                    "category": "Sexually Explicit",
                    "severityScore": 0.1
                  }
                ],
                "blocked": false
              }
            }
          ],
          "metadata": {
            "tokenMetadata": {
              "outputTokenCount": {
                "totalBillableCharacters": 123,
                "totalTokens": 30
              },
              "inputTokenCount": {
                "totalTokens": 7,
                "totalBillableCharacters": 27
              }
            }
          }
        }
     */
}
