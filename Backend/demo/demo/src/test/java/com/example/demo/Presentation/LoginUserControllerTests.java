package com.example.demo.Presentation;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Config.SecurityHelper;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginUserControllerTests {

    private LoginUserController controller;
    private UserRepository userRepository;
    private SecurityHelper securityHelper;
    private Label errorLabel;
    private PasswordField passwordField;
    private TextField usernameField;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize the JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        securityHelper = mock(SecurityHelper.class);
        controller = new LoginUserController(userRepository, securityHelper);
        errorLabel = new Label();
        passwordField = new PasswordField();
        usernameField = new TextField();
        ReflectionTestUtils.setField(controller, "errorLabel", errorLabel);
        ReflectionTestUtils.setField(controller, "passwordField", passwordField);
        ReflectionTestUtils.setField(controller, "usernameField", usernameField);
    }

    @Test
    public void testGoBackOnAction() {
        errorLabel.setText("");
        controller.goBackOnAction(new ActionEvent());
        // In our test environment, JavaFXApplication.switchScene will throw an exception,
        // so errorLabel is expected to be set accordingly.
        assertEquals("Unable to load start page.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_EmptyFields() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText("");
        controller.loginButtonOnAction(new ActionEvent());
        assertEquals("Please fill in all fields.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_InvalidUsername() {
        usernameField.setText("nonexistent");
        passwordField.setText("somePassword");
        errorLabel.setText("");
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);
        controller.loginButtonOnAction(new ActionEvent());
        assertEquals("Invalid username.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_InvalidPassword() {
        usernameField.setText("testUser");
        passwordField.setText("wrongPassword");
        errorLabel.setText("");
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("hashedCorrect");
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(securityHelper.hashPassword("wrongPassword")).thenReturn("hashedWrong");
        controller.loginButtonOnAction(new ActionEvent());
        assertEquals("Invalid password.", errorLabel.getText());
    }

    @Test
    public void testLoginButtonOnAction_ValidLogin() {
        usernameField.setText("testUser");
        passwordField.setText("correctPassword");
        errorLabel.setText("");
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("hashedCorrect");
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(securityHelper.hashPassword("correctPassword")).thenReturn("hashedCorrect");
        controller.loginButtonOnAction(new ActionEvent());
        // In the test environment, JavaFXApplication.switchScene("MainPage.fxml", user)
        // will throw an exception, so errorLabel should be updated accordingly.
        assertEquals("Failed to load main page.", errorLabel.getText());
    }
}
