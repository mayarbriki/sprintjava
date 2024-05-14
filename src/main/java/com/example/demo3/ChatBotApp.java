package com.example.demo3;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Properties;

public class ChatBotApp extends Application {

    private ChatBot chatBot = new ChatBot();
    private TextArea chatArea;

    public ChatBotApp() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX ChatBot");

        // Interface components
        TextField inputField = new TextField();
        inputField.setPromptText("Type your message here");
        inputField.setOnAction(e -> {
            try {
                processUserInput(inputField.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            try {
                processUserInput(inputField.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        VBox chatLayout = new VBox(10);
        chatLayout.getChildren().addAll(chatArea, inputField, sendButton);
        chatLayout.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(chatLayout, 400, 300));
        primaryStage.show();
    }

    private void processUserInput(String input) throws IOException {
        // Display user message
        displayMessage("You: " + input);

        // Process input with the chatbot
        String response = chatBot.getResponse(input);

        // Display chatbot response
        displayMessage("Bot: " + response);
    }


    private void displayMessage(String message) {
        chatArea.appendText(message + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}



