package com.example.demo.Presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainAdminController {

    @FXML
    private Button homeButton;

    @FXML
    private Label incomeDisplay;

    @FXML
    private Button loggedUsers;

    @FXML
    private Button manageCategoriesButton;

    @FXML
    private Label usernameDisplay;

    @FXML
    void homeButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("MainAdminPage.fxml");

    }
    @FXML
    void manageCategoriesButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("CategoriesPage.fxml");

    }
    @FXML
    void loggedUsersOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("MainAdminPage.fxml");
    }
    @FXML
    void goBackOnAction(ActionEvent event) {
        try {
            JavaFXApplication.switchScene("StartPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
