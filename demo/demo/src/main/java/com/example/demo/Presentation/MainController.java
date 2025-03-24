package com.example.demo.Presentation;

import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {

    @FXML
    private Label cardsDisplay;

    @FXML
    private Label cardsOwn;

    @FXML
    private Button chartExpensesButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button homeButton;

    @FXML
    private Label incomeDisplay;

    @FXML
    private Button manageCardsButton;

    @FXML
    private Button manageExpenses;

    @FXML
    private Label usernameDisplay;

    // Store the current user.
    private User currentUser;

    /**
     * This method is automatically called when the FXML is loaded.
     */
    @FXML
    public void initialize() {
        // Set default texts until a user is provided.
        usernameDisplay.setText("User: N/A");
        incomeDisplay.setText("Income: N/A");
        cardsOwn.setText("");
        cardsDisplay.setText("");
    }

    /**
     * Set the current user and update the display labels.
     *
     * @param user the logged-in user.
     */
    public void setUser(User user) {
        this.currentUser = user;
        usernameDisplay.setText(user.getUsername());
        incomeDisplay.setText("" + user.getSalary());

        // Check if the user has any cards (UserBudget entries)
        if (user.getUserBudgets() != null && !user.getUserBudgets().isEmpty()) {
            cardsOwn.setText("Cards Owned:");
            StringBuilder sb = new StringBuilder();
            for (UserBudget ub : user.getUserBudgets()) {
                if (ub.getBudget() != null) {
                    sb.append("• Card Number: ")
                            .append(ub.getBudget().getCardnumber())
                            .append(" | Budget: ")
                            .append(ub.getBudget().getAmount())
                            .append("\n");
                }
            }
            cardsDisplay.setText(sb.toString());
        } else {
            cardsOwn.setText("No Cards Owned");
            cardsDisplay.setText("");
        }
    }

    @FXML
    void chartExpensesButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ChartPage.fxml", this.currentUser);
    }

    @FXML
    void historyButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("HistoryPage.fxml", this.currentUser);    }

    @FXML
    void homeButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("MainPage.fxml", this.currentUser);    }

    @FXML
    void manageCardsButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ManageCardsPage.fxml", this.currentUser);    }

    @FXML
    void manageExpensesOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ManageExpenses.fxml", this.currentUser);   }
    }

