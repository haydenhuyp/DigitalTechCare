package com.techcare.techcare;

import android.os.AsyncTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/* For calling the Google Cloud Translation API. */
public class TranslatorHelper{

    // Instantiates the OkHttpClient.
    OkHttpClient client = new OkHttpClient();

    // This function performs a POST request.
    public String Post(String text) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\n" +
                        "  \"q\": \""+ text +"\",\n" +
                        "  \"source\": \"en\",\n" +
                        "  \"target\": \"ko\",\n" +
                        "  \"format\": \"text\"\n" +
                        "}");
        Request request = new Request.Builder()
                .url("https://translation.googleapis.com/language/translate/v2")
                .post(body)
                .addHeader("Authorization", "Bearer " + DataUtility.GCP_ACCESS_TOKEN)
                .addHeader("x-goog-user-project", DataUtility.GCP_PROJECT_ID)
                .addHeader("Content-type", "application/json; charset=UTF-8")
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

    public static String execute(String text){
        try {
            TranslatorHelper request = new TranslatorHelper();
            String response = request.Post(text);
            // parse the response to TranslatorResponse to get the translated text
            Gson gson = new Gson();
            TranslatorResponse translatorResponse = gson.fromJson(response, TranslatorResponse.class);
            return translatorResponse.data.translations[0].translatedText;
        } catch (Exception e) {
            return "";
        }
    }

    protected class TranslatorResponse{
        public Data data;
        protected class Data{
            protected Translation[] translations;
            protected class Translation{
                protected String translatedText;
            }
        }
    }
    /* Sample response
    {
      "data": {
        "translations": [
          {
            "translatedText": "안녕하세요. 어떻게 지내세요?"
          }
        ]
      }
    }
    */
}
