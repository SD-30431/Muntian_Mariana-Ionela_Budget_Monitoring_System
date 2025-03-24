package com.example.demo.Presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

@Component
public class StartController {

    private final JavaFXApplication app;

    public StartController(JavaFXApplication app) {
        this.app = app;
    }

    @FXML
    private Button createNewAccount;

    @FXML
    private Button loginAdmin;

    @FXML
    private Button loginUser;


    @FXML
    void createNewAccountOnAction(ActionEvent event) {
        switchScene("SinginUserPage.fxml", "Sign Up");
    }

    @FXML
    void loginAdminOnAction(ActionEvent event) {
        switchScene("LoginAdminPage.fxml", "Admin Login");
    }

    @FXML
    void loginUserOnAction(ActionEvent event) {
        switchScene("LoginUserPage.fxml", "User Login");
    }

    /**
     * Switches the scene using JavaFXApplication.
     *
     * @param fxmlPath The FXML file path.
     * @param title    The title of the new scene.
     */
    private void switchScene(String fxmlPath, String title) {
        try {
            JavaFXApplication.switchScene(fxmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
