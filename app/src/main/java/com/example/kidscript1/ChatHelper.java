package com.example.kidscript1;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ChatHelper {
    private static final String API_URL = "https://api.gemini.ai/v1/engines/gemini/completions"; // Replace with the desired Gemini API endpoint

    public static String generateText(String inputText) {
        OkHttpClient client = new OkHttpClient();

        String apiKey = System.getenv("AIzaSyB9ZASoeDATC-9_39FLCUOLOuLR9R5rnng"); // Assuming you store the API key in an environment variable

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("prompt", inputText);
            jsonObject.put("max_tokens", 50); // Adjust the token limit as needed

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "Error: " + response.code() + " - " + response.message();
            }

            String responseBody = response.body().string();
            JSONObject responseJson = new JSONObject(responseBody);
            return responseJson.getJSONArray("choices").getJSONObject(0).getString("text");

        } catch (JSONException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            return "An error occurred. Please try again later."; // User-friendly error message
        } catch (IOException e) {
            System.err.println("Error making network request: " + e.getMessage());
            return "An error occurred. Please try again later."; // User-friendly error message
        }
    }
}