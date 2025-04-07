package com.example.demo.Presentation;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Config.SecurityHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginUserController {

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

    private final UserRepository userRepository;
    private final SecurityHelper securityHelper;

    @Autowired
    public LoginUserController(UserRepository userRepository, SecurityHelper securityHelper) {
        this.userRepository = userRepository;
        this.securityHelper = securityHelper;
    }

    /**
     * Called when the "Go Back" button is pressed.
     * Switches back to the StartPage.
     */
    @FXML
    void goBackOnAction(ActionEvent event) {
        try {
            // No user is passed since we are returning to the start page.
            JavaFXApplication.switchScene("StartPage.fxml");
        } catch (Exception e) {
            errorLabel.setText("Unable to load start page.");
            e.printStackTrace();
        }
    }

    /**
     * Called when the "Login" button is pressed.
     * Retrieves the user from the database using the username,
     * hashes the entered password via SecurityHelper, and compares it to the stored hash.
     * On successful authentication, switches to the MainPage and passes the user.
     */
    @FXML
    void loginButtonOnAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        // Retrieve the user by username.
        User user = userRepository.findByUsername(username);
        if (user == null) {
            errorLabel.setText("Invalid username.");
            return;
        }

        // Hash the input password and compare it to the stored hashed password.
        String hashedInputPassword = securityHelper.hashPassword(password);
        if (!user.getPasswordHash().equals(hashedInputPassword)) {
            errorLabel.setText("Invalid password.");
            return;
        }

        try {
            JavaFXApplication.switchScene("MainPage.fxml", user);
        } catch (Exception e) {
            errorLabel.setText("Failed to load main page.");
            e.printStackTrace();
        }
    }
}
