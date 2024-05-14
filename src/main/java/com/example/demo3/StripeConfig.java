package com.example.demo3;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.scene.Scene;
import services.ServicePanier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List; // Import List
import models.Panier;// Assuming Product is your model class for products
import javafx.stage.Stage;
import utils.MyDatabase;

public class StripeConfig {

    @FXML
    private TextField amountField;

    @FXML
    private TextField currencyField;

    @FXML
    private TextField cardNumberField;





    public void setAmountToPay(float amount) {
        // Set the payment amount in the amountField TextField
        amountField.setText(String.valueOf(amount));
    }



    @FXML
    public void processPayment() {
        String amountText = amountField.getText();
        String currency = currencyField.getText();
        String card = cardNumberField.getText();

        // Move the payment processing logic here
        try {
            Stripe.apiKey = "sk_test_51P9BCeRrGFzQxRXy2XKBKsNkkBoQDNeuXYTN9yaj5CIfC0SO3M2geINY1LQZdCSdSc9tigNoTor4Hwb5L5jKKA4c00zL2qo1Hj"; // Your Secret Key

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (Float.valueOf(amountText) * 1)) // Amount in cents
                    .setCurrency(currency)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());

            // Clear the text fields after successful payment
            amountField.clear();
            currencyField.clear();
            sup();


            // Close the payment window
            Stage stage = (Stage) amountField.getScene().getWindow();
            stage.close();

            // Refresh the panier table


        } catch (StripeException e) {
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }

    private void sup() {
        // Confirm with the user before deleting all entries

        try {
            // Get connection from DataSource
            Connection connection = MyDatabase.getInstance().getConnection();

            // Create statement
            Statement statement = connection.createStatement();

            // Execute SQL DELETE query to delete all entries from "panier" table
            String sql = "DELETE  FROM panier";
            statement.executeUpdate(sql);



            // Refresh the table after deletion

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}