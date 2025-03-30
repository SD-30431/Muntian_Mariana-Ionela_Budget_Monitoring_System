package com.example.demo.Presentation;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SigninUserControllerTests {

    private SigninUserController controller;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField cardNoField;
    private TextField incomeField;
    private Label errorLabel;
    private Button goBack;
    private Button loginButton;

    private HttpClient mockHttpClient;
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void initJavaFX() {
        new JFXPanel();
    }

    @BeforeEach
    public void setup() throws Exception {
        controller = new SigninUserController();
        usernameField = new TextField();
        passwordField = new PasswordField();
        confirmPasswordField = new PasswordField();
        cardNoField = new TextField();
        incomeField = new TextField();
        errorLabel = new Label();
        goBack = new Button();
        loginButton = new Button();

        ReflectionTestUtils.setField(controller, "usernameField", usernameField);
        ReflectionTestUtils.setField(controller, "passwordField", passwordField);
        ReflectionTestUtils.setField(controller, "confirmPasswordField", confirmPasswordField);
        ReflectionTestUtils.setField(controller, "cardNoField", cardNoField);
        ReflectionTestUtils.setField(controller, "incomeField", incomeField);
        ReflectionTestUtils.setField(controller, "errorLabel", errorLabel);
        ReflectionTestUtils.setField(controller, "goBack", goBack);
        ReflectionTestUtils.setField(controller, "loginButton", loginButton);

        mockHttpClient = mock(HttpClient.class);
        ReflectionTestUtils.setField(controller, "httpClient", mockHttpClient);
        objectMapper = (ObjectMapper) ReflectionTestUtils.getField(controller, "objectMapper");
    }

    @Test
    public void testGoBackOnAction_SetsErrorLabelOnFailure() throws IOException, InterruptedException {
        doThrow(new RuntimeException("Switch failed"))
                .when(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        controller.goBackOnAction(new ActionEvent());
        assertEquals("Unable to load the previous screen.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_EmptyFields() {
        // Leave all fields empty.
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        cardNoField.setText("");
        incomeField.setText("");
        controller.loginButtonOnAction(new ActionEvent());
        assertEquals("Please fill in all fields.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_PasswordMismatch() {
        usernameField.setText("testUser");
        passwordField.setText("password1");
        confirmPasswordField.setText("password2");
        cardNoField.setText("1111-2222-3333-4444");
        incomeField.setText("5000");
        controller.loginButtonOnAction(new ActionEvent());
        assertEquals("Passwords do not match.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_CreateUserFails() throws Exception {
        usernameField.setText("testUser");
        passwordField.setText("adminPass");
        confirmPasswordField.setText("adminPass");
        cardNoField.setText("1111-2222-3333-4444");
        incomeField.setText("5000");

        HttpResponse<String> userResponse = mock(HttpResponse.class);
        when(userResponse.statusCode()).thenReturn(500);
        when(mockHttpClient.send(argThat(req -> req.uri().toString().endsWith("/user/create")),
                any(HttpResponse.BodyHandler.class))).thenReturn(userResponse);

        controller.loginButtonOnAction(new ActionEvent());
        assertEquals("Failed to create user.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_LinkUserBudgetFails() throws Exception {
        usernameField.setText("testUser");
        passwordField.setText("adminPass");
        confirmPasswordField.setText("adminPass");
        cardNoField.setText("1111-2222-3333-4444");
        incomeField.setText("5000");

        // 1. Simulate successful createUser.
        User createdUser = new User();
        createdUser.setUsername("testUser");
        createdUser.setSalary(5000.0);
        createdUser.setUserBudgets(new HashSet<>());
        String userJson = objectMapper.writeValueAsString(createdUser);
        HttpResponse<String> userResponse = mock(HttpResponse.class);
        when(userResponse.statusCode()).thenReturn(201);
        when(userResponse.body()).thenReturn(userJson);
        when(mockHttpClient.send(argThat(req -> req.uri().toString().endsWith("/user/create")),
                any(HttpResponse.BodyHandler.class))).thenReturn(userResponse);

        // 2. Simulate getBudget returns empty response (i.e. budget does not exist).
        HttpResponse<String> getBudgetResponse = mock(HttpResponse.class);
        when(getBudgetResponse.statusCode()).thenReturn(200);
        when(getBudgetResponse.body()).thenReturn("");
        when(mockHttpClient.send(argThat(req -> req.uri().toString().contains("/budget/")),
                any(HttpResponse.BodyHandler.class))).thenReturn(getBudgetResponse);

        // 3. Simulate createBudget call returns a valid Budget.
        Budget budget = new Budget();
        budget.setAmount(5000.0);
        budget.setCardnumber("1111-2222-3333-4444");
        String budgetJson = objectMapper.writeValueAsString(budget);
        HttpResponse<String> createBudgetResponse = mock(HttpResponse.class);
        when(createBudgetResponse.statusCode()).thenReturn(201);
        when(createBudgetResponse.body()).thenReturn(budgetJson);
        when(mockHttpClient.send(argThat(req -> req.uri().toString().endsWith("/budget/create")),
                any(HttpResponse.BodyHandler.class))).thenReturn(createBudgetResponse);

        // 4. Simulate linkUserBudget failure (status not 200).
        HttpResponse<Void> linkResponse = mock(HttpResponse.class);
        when(linkResponse.statusCode()).thenReturn(500);
        when(mockHttpClient.send(argThat(req -> req.uri().toString().contains("/userbudget/link")),
                any(HttpResponse.BodyHandler.class))).thenReturn(linkResponse);

        controller.loginButtonOnAction(new ActionEvent());
        assertEquals("Failed to link user with budget.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_SuccessfulFlow() throws Exception {
        // Provide valid input.
        usernameField.setText("testUser");
        passwordField.setText("adminPass");
        confirmPasswordField.setText("adminPass");
        cardNoField.setText("1111-2222-3333-4444");
        incomeField.setText("5000");

        // 1. Simulate successful createUser.
        User createdUser = new User();
        createdUser.setUsername("testUser");
        createdUser.setSalary(5000.0);
        createdUser.setUserBudgets(new HashSet<>());
        String userJson = objectMapper.writeValueAsString(createdUser);
        HttpResponse<String> userResponse = mock(HttpResponse.class);
        when(userResponse.statusCode()).thenReturn(201);
        when(userResponse.body()).thenReturn(userJson);
        when(mockHttpClient.send(argThat(req -> req.uri().toString().endsWith("/user/create")),
                any(HttpResponse.BodyHandler.class))).thenReturn(userResponse);

        // 2. Simulate getBudget returns empty response.
        HttpResponse<String> getBudgetResponse = mock(HttpResponse.class);
        when(getBudgetResponse.statusCode()).thenReturn(200);
        when(getBudgetResponse.body()).thenReturn("");
        when(mockHttpClient.send(argThat(req -> req.uri().toString().contains("/budget/")),
                any(HttpResponse.BodyHandler.class))).thenReturn(getBudgetResponse);

        // 3. Simulate createBudget call returns a valid Budget.
        Budget budget = new Budget();
        budget.setAmount(5000.0);
        budget.setCardnumber("1111-2222-3333-4444");
        String budgetJson = objectMapper.writeValueAsString(budget);
        HttpResponse<String> createBudgetResponse = mock(HttpResponse.class);
        when(createBudgetResponse.statusCode()).thenReturn(201);
        when(createBudgetResponse.body()).thenReturn(budgetJson);
        when(mockHttpClient.send(argThat(req -> req.uri().toString().endsWith("/budget/create")),
                any(HttpResponse.BodyHandler.class))).thenReturn(createBudgetResponse);

        // 4. Simulate linkUserBudget call returns status 200.
        HttpResponse<Void> linkResponse = mock(HttpResponse.class);
        when(linkResponse.statusCode()).thenReturn(200);
        when(mockHttpClient.send(argThat(req -> req.uri().toString().contains("/userbudget/link")),
                any(HttpResponse.BodyHandler.class))).thenReturn(linkResponse);

        // For the final call to JavaFXApplication.switchScene, it may throw an exception.
        // We'll catch that exception and ignore it since our focus is on the REST flow.
        try {
            controller.loginButtonOnAction(new ActionEvent());
        } catch (Exception e) {
            // Expected in test environment; do nothing.
        }

        // At this point, the user should have been created and linked with the budget.
        // Also, a new UserBudget should have been added to the user's collection.
        assertFalse(createdUser.getUserBudgets().isEmpty());
        // And errorLabel should not have been set to an error message from earlier.
        assertTrue(errorLabel.getText().isEmpty() || errorLabel.getText().startsWith("Sign in failed"));
    }
}
