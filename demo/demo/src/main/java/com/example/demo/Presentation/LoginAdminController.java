package com.example.demo.Presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class LoginAdminController {

    @FXML
    private Label errorLabel;

    @FXML
    private Button goBack;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    /**
     * Called when the "Go Back" button is pressed.
     * Switches back to the start page.
     */
    @FXML
    void goBackOnAction(ActionEvent event) {
        try {
            JavaFXApplication.switchScene("StartPage.fxml");
        } catch (Exception e) {
            errorLabel.setText("Unable to load start page.");
            e.printStackTrace();
        }
    }

    /**
     * Called when the "Login" button is pressed.
     * Checks if the entered username and password both equal "admin".
     * On success, it switches to the Admin main page.
     */
    @FXML
    void loginButtonOnAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (!username.equals("admin") || !password.equals("admin")) {
            errorLabel.setText("Invalid admin credentials.");
            return;
        }

        try {
            // On successful authentication, switch to the admin main page.
            JavaFXApplication.switchScene("MainAdminPage.fxml");
        } catch (Exception e) {
            errorLabel.setText("Failed to load admin main page.");
            e.printStackTrace();
        }
    }
}
