package com.example.demo.Presentation;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class LoginAdminControllerTests {

    private LoginAdminController controller;
    private Label errorLabel;
    private Button goBack;
    private Button loginButton;
    private PasswordField passwordField;
    private TextField usernameField;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        controller = new LoginAdminController();
        errorLabel = new Label();
        goBack = new Button();
        loginButton = new Button();
        passwordField = new PasswordField();
        usernameField = new TextField();

        ReflectionTestUtils.setField(controller, "errorLabel", errorLabel);
        ReflectionTestUtils.setField(controller, "goBack", goBack);
        ReflectionTestUtils.setField(controller, "loginButton", loginButton);
        ReflectionTestUtils.setField(controller, "passwordField", passwordField);
        ReflectionTestUtils.setField(controller, "usernameField", usernameField);
    }

    @Test
    public void testGoBackOnAction() {
        errorLabel.setText("");
        controller.goBackOnAction(new ActionEvent());
        // Since JavaFXApplication.switchScene("StartPage.fxml") will throw an exception
        // in the test environment, errorLabel should be set accordingly.
        assertEquals("Unable to load start page.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_InvalidCredentials() {
        usernameField.setText("user");
        passwordField.setText("wrong");
        errorLabel.setText("");
        controller.loginButtonOnAction(new ActionEvent());
        // With invalid credentials, the errorLabel should indicate invalid admin credentials.
        assertEquals("Invalid admin credentials.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_ValidCredentials() {
        usernameField.setText("admin");
        passwordField.setText("admin");
        errorLabel.setText("");
        controller.loginButtonOnAction(new ActionEvent());
        // With valid credentials, the method attempts to switch scene.
        // In the test environment, an exception is thrown, so errorLabel is updated.
        assertEquals("Failed to load admin main page.", errorLabel.getText());
    }
}
