package com.example.demo.Presentation;

import com.example.demo.Model.User;
import com.example.demo.Model.Budget;
import com.example.demo.Model.UserBudget;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class SigninUserController {

    @FXML
    private TextField cardNoField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button goBack;

    @FXML
    private TextField incomeField;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    // Base URL for your REST API endpoints (adjust if needed)
    private static final String BASE_URL = "http://localhost:8080";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    void goBackOnAction(ActionEvent event) {
        try {
            JavaFXApplication.switchScene("StartPage.fxml");
        } catch (Exception e) {
            errorLabel.setText("Unable to load the previous screen.");
            e.printStackTrace();
        }
    }

    @FXML
    void loginButtonOnAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String cardNo = cardNoField.getText().trim();
        String incomeText = incomeField.getText().trim();

        // Validate that all fields are filled
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                cardNo.isEmpty() || incomeText.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        // Check that the passwords match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        // Validate and parse the income field
        double income;
        try {
            income = Double.parseDouble(incomeText);
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid income value.");
            return;
        }

        try {
            // 1. Create the user via the REST endpoint (/user/create)
            User createdUser = createUser(username, password, income);
            if (createdUser == null) {
                errorLabel.setText("Failed to create user.");
                return;
            }

            // 2. Check if a Budget exists for the provided card number (GET /budget/{cardnumber})
            Budget budget = getBudget(cardNo);
            if (budget == null) {
                // If it doesn't exist, create a new budget (POST /budget/create)
                // Here we assume the initial budget amount equals the income.
                budget = createBudget(income, cardNo);
                if (budget == null) {
                    errorLabel.setText("Failed to create budget.");
                    return;
                }
            }

            // 3. Link the user to the budget (POST /userbudget/link?username=...&cardnumber=...)
            boolean linked = linkUserBudget(username, cardNo);
            if (!linked) {
                errorLabel.setText("Failed to link user with budget.");
                return;
            }

            createdUser.getUserBudgets().add(new UserBudget(createdUser, budget));

            // 4. Switch to the main screen passing the created user.
            JavaFXApplication.switchScene("MainPage.fxml", createdUser);
        } catch (Exception e) {
            errorLabel.setText("Sign in failed: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private User createUser(String username, String password, double salary) throws Exception {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password); // The backend should perform password hashing.
        user.setSalary(salary);

        String json = objectMapper.writeValueAsString(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), User.class);
        }
        return null;
    }

    private Budget getBudget(String cardNo) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/budget/" + cardNo))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                return null;
            }
            return objectMapper.readValue(responseBody, Budget.class);
        }
        return null;
    }

    private Budget createBudget(double amount, String cardNo) throws Exception {
        Budget budget = new Budget();
        budget.setAmount(amount);
        budget.setCardnumber(cardNo);

        String json = objectMapper.writeValueAsString(budget);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/budget/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Budget.class);
        }
        return null;
    }

    private boolean linkUserBudget(String username, String cardNo) throws Exception {
        String url = String.format(BASE_URL + "/userbudget/link?username=%s&cardnumber=%s", username, cardNo);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        return response.statusCode() == 200;
    }
}
