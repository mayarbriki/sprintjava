package services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

public class PaymentService {

    public static void main(String[] args) {

        Stripe.apiKey = "sk_test_51P9BCeRrGFzQxRXy2XKBKsNkkBoQDNeuXYTN9yaj5CIfC0SO3M2geINY1LQZdCSdSc9tigNoTor4Hwb5L5jKKA4c00zL2qo1Hj";
        try {
// Retrieve your account information
            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
// Print other account information as needed
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}