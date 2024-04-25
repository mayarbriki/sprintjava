package com.example.demo3;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class ChatBot {
    private final OkHttpClient client;

    public ChatBot() {
        this.client = new OkHttpClient();
    }

    public String getResponse(String input) {
        String apiKey = "sk-proj-JYSVDQoSSB8PVPyZZeRpT3BlbkFJjPtQyc1X83q5pqzsnGNV";
        String url = "https://api.openai.com/v1/completions";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        // Update the model name as per the OpenAI API documentation
        String jsonInput = "{\"prompt\": \"" + input + "\", \"max_tokens\": 50, \"model\": \"gpt-3.5-turbo-instruct\"}";

        System.out.println("Request JSON: " + jsonInput); // Log request JSON

        RequestBody body = RequestBody.create(jsonInput, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println("Response body: " + responseBody); // Log response body

            if (!response.isSuccessful()) {
                System.err.println("Unexpected response code: " + response.code());
                throw new IOException("Unexpected code " + response.code());
            }

            return parseGeneratedText(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("Error communicating with the OpenAI API", e);
        }
    }

    private String parseGeneratedText(String responseBody) {
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
        if (choicesArray != null && choicesArray.size() > 0) {
            JsonElement firstChoice = choicesArray.get(0);
            if (firstChoice.isJsonObject()) {
                JsonObject choiceObject = firstChoice.getAsJsonObject();
                JsonElement textElement = choiceObject.get("text");
                if (textElement != null && textElement.isJsonPrimitive()) {
                    return textElement.getAsString();
                }
            }
        }
        throw new RuntimeException("Failed to parse generated text from API response");
    }

    public static void main(String[] args) {
        try {
            ChatBot chatBot = new ChatBot();
            String input = "Hello, how are you?";
            String response = chatBot.getResponse(input);
            System.out.println("Response:\n" + response);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
