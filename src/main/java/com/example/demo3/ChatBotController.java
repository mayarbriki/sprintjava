package com.example.demo3;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.example.demo3.ChatBot;

import java.io.IOException;


public class ChatBotController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    private ChatBot chatBot = new ChatBot(); // Assuming ChatBot is a separate class

    public ChatBotController() throws IOException {
    }

    public void sendMessage() throws IOException {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Process the message with the chatbot
            String response = chatBot.getResponse(message);

            // Display user message and chatbot response
            displayMessage("You: " + message);
            displayMessage("Bot: " + response);

            inputField.clear(); // Clear the input field after sending
        }
    }

    private void displayMessage(String message) {
        chatArea.appendText(message + "\n");
    }
}
