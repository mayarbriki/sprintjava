package com.example.demo3;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class FacebookPoster {
    private static final String USER_ACCESS_TOKEN = "your-user-access-token";
    private static final String PAGE_ID = "your-page-id";
    private static final String POST_MESSAGE = "Hello, Facebook! This is a test post.";

    public static void main(String[] args) throws IOException {
        postOnFacebook(USER_ACCESS_TOKEN, POST_MESSAGE);
    }

    private static void postOnFacebook(String accessToken, String message) throws IOException {
        String graphApiUrl = "https://graph.facebook.com/" + PAGE_ID + "/feed";

        URL url = new URL(graphApiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        // Construct the message data
        String postData = "message=" + URLEncoder.encode(message, "UTF-8") +
                "&access_token=" + URLEncoder.encode(accessToken, "UTF-8");

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = postData.getBytes("utf-8");
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Handle response as needed
        connection.disconnect();
    }
}

}
