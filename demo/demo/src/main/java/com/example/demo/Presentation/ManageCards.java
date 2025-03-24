package com.example.demo.Presentation;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.BudgetRepository;
import com.example.demo.Repository.UserBudgetRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ManageCards {

    @FXML
    private Button AddNewCard;

    @FXML
    private TextField amountField;

    @FXML
    private TextField cardNoField;

    @FXML
    private Button chartExpensesButton;

    @FXML
    private Rectangle form;

    @FXML
    private Button goBack;

    @FXML
    private Button historyButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button manageCardsButton;

    @FXML
    private Button manageExpenses;

    @FXML
    private Button saveButton;

    // Store the current user.
    private User currentUser;

    // Repositories injected via constructor.
    private final BudgetRepository budgetRepository;
    private final UserBudgetRepository userBudgetRepository;

    public ManageCards(BudgetRepository budgetRepository, UserBudgetRepository userBudgetRepository) {
        this.budgetRepository = budgetRepository;
        this.userBudgetRepository = userBudgetRepository;
    }

    @FXML
    public void initialize() {
        // Hide the form at startup.
        form.setVisible(false);
    }

    /**
     * This method is called from the scene switching logic to pass the logged-in user.
     *
     * @param user the current user.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    void AddNewCardOnAction(ActionEvent event) {
        // Show the form so the user can enter a new card.
        form.setVisible(true);
    }

    @FXML
    void chartExpensesButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ChartPage.fxml", currentUser);
    }

    @FXML
    void goBackOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("StartPage.fxml", currentUser);
    }

    @FXML
    void historyButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("HistoryPage.fxml", currentUser);
    }

    @FXML
    void homeButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("MainPage.fxml", currentUser);
    }

    @FXML
    void manageCardsButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ManageCards.fxml", currentUser);
    }

    @FXML
    void manageExpensesOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ManageExpenses.fxml", currentUser);
    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
        try {
            // Validate input: amount and card number must be provided.
            String amountText = amountField.getText();
            String cardNumber = cardNoField.getText();

            if (amountText == null || amountText.isEmpty() ||
                    cardNumber == null || cardNumber.trim().isEmpty()) {
                System.out.println("Amount and Card Number are required.");
                return;
            }

            double amount = Double.parseDouble(amountText);

            // Create a new Budget instance.
            Budget newBudget = new Budget(amount, cardNumber);
            newBudget = budgetRepository.save(newBudget);

            // Create a new UserBudget linking the current user and the new budget.
            UserBudget userBudget = new UserBudget(currentUser, newBudget);
            userBudget = userBudgetRepository.save(userBudget);

            // Add the new UserBudget to the current user's list.
            currentUser.getUserBudgets().add(userBudget);

            System.out.println("New card added with card number: " + cardNumber);

            // Hide the form and clear fields.
            form.setVisible(false);
            amountField.clear();
            cardNoField.clear();
        } catch (Exception e) {
            System.out.println("Error saving new card: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
