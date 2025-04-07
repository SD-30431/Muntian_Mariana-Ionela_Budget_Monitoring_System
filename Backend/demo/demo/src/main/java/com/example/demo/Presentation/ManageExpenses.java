package com.example.demo.Presentation;

import com.example.demo.Model.Budget;
import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.BudgetRepository;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class ManageExpenses {

    @FXML
    private TextField IDField;

    @FXML
    private Label IDLabel;

    @FXML
    private Button addNewProductButton;

    @FXML
    private ComboBox<String> cardNoField;

    @FXML
    private Label cardNoLabel;

    @FXML
    private ComboBox<String> categoryField;

    @FXML
    private Label categoryLabel;

    @FXML
    private Button chartExpensesButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private Label dateLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editProductButton;

    @FXML
    private Rectangle form;

    @FXML
    private Button historyButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button manageCardsButton;

    @FXML
    private Button manageExpenses;

    @FXML
    private TextField nameField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField priceField;

    @FXML
    private Label priceLabel;

    @FXML
    private Button saveButton;

    private enum OperationMode { NONE, ADD, EDIT, DELETE }
    private OperationMode currentMode = OperationMode.NONE;

    private User currentUser;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;

    public ManageExpenses(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          BudgetRepository budgetRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
    }

    @FXML
    public void initialize() {
        form.setVisible(false);
        setFormControlsVisibility(false, false, false);

        categoryField.getItems().clear();
        categoryRepository.findAll().forEach(cat -> categoryField.getItems().add(cat.getName()));
    }

    /**
     * Sets the visibility of the form controls.
     *
     * @param showId     If true, show the ID field and label.
     * @param showOthers If true, show card, category, date, name, and price fields.
     * @param showSave   If true, show the save button.
     */
    private void setFormControlsVisibility(boolean showId, boolean showOthers, boolean showSave) {
        IDField.setVisible(showId);
        IDLabel.setVisible(showId);

        cardNoField.setVisible(showOthers);
        cardNoLabel.setVisible(showOthers);
        categoryField.setVisible(showOthers);
        categoryLabel.setVisible(showOthers);
        dateField.setVisible(showOthers);
        dateLabel.setVisible(showOthers);
        nameField.setVisible(showOthers);
        nameLabel.setVisible(showOthers);
        priceField.setVisible(showOthers);
        priceLabel.setVisible(showOthers);

        saveButton.setVisible(showSave);
    }

    // Overloaded helper: always show the save button.
    private void setFormControlsVisibility(boolean showId, boolean showOthers) {
        setFormControlsVisibility(showId, showOthers, true);
    }

    /**
     * Custom method for DELETE mode: only the ID and card number fields are visible.
     */
    private void setDeleteFormControlsVisibility() {
        IDField.setVisible(true);
        IDLabel.setVisible(true);

        cardNoField.setVisible(true);
        cardNoLabel.setVisible(true);

        categoryField.setVisible(false);
        categoryLabel.setVisible(false);
        dateField.setVisible(false);
        dateLabel.setVisible(false);
        nameField.setVisible(false);
        nameLabel.setVisible(false);
        priceField.setVisible(false);
        priceLabel.setVisible(false);

        saveButton.setVisible(true);
    }

    private void showFormForAdd() {
        currentMode = OperationMode.ADD;
        form.setVisible(true);
        // For add: hide the ID field, show the others.
        setFormControlsVisibility(false, true);
        saveButton.setText("Save");
        clearFormFields();
    }

    private void showFormForEdit() {
        currentMode = OperationMode.EDIT;
        form.setVisible(true);
        // For edit: show all fields.
        setFormControlsVisibility(true, true);
        saveButton.setText("Update");
        clearFormFields();
    }

    private void showFormForDelete() {
        currentMode = OperationMode.DELETE;
        form.setVisible(true);
        // For delete: only show the ID and card number fields.
        setDeleteFormControlsVisibility();
        saveButton.setText("Delete");
        clearFormFields();
    }

    private void clearFormFields() {
        IDField.clear();
        dateField.setValue(null);
        nameField.clear();
        priceField.clear();
        cardNoField.getSelectionModel().clearSelection();
        categoryField.getSelectionModel().clearSelection();
    }

    @FXML
    void addNewProductButtonOnAction(ActionEvent event) {
        showFormForAdd();
    }

    @FXML
    void editProductButtonOnAction(ActionEvent event) {
        showFormForEdit();
    }

    @FXML
    void deleteButtonOnAction(ActionEvent event) {
        showFormForDelete();
    }

    @FXML
    void chartExpensesButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ChartPage.fxml", currentUser);
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
        JavaFXApplication.switchScene("ManageCardsPage.fxml", currentUser);
    }

    @FXML
    void manageExpensesOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ManageExpenses.fxml", currentUser);
    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
        try {
            switch (currentMode) {
                case ADD:
                    addProduct();
                    break;
                case EDIT:
                    editProduct();
                    break;
                case DELETE:
                    deleteProduct();
                    break;
                default:
                    System.out.println("No operation mode selected.");
            }
        } catch (Exception e) {
            System.out.println("Error processing the operation: " + e.getMessage());
            e.printStackTrace();
        }
        // Hide the form and reset the operation mode.
        form.setVisible(false);
        setFormControlsVisibility(false, false, false);
        currentMode = OperationMode.NONE;
    }

    private void addProduct() {
        String selectedCard = cardNoField.getValue();
        String categoryName = categoryField.getValue();
        LocalDate date = dateField.getValue();
        String productName = nameField.getText();
        double price = Double.parseDouble(priceField.getText());

        // Lookup the category; if not found, create a new one.
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            category = new Category(categoryName);
            category = categoryRepository.save(category);
            categoryField.getItems().add(category.getName());
        }

        // Create and save the new product.
        Product product = new Product(productName, price, date, category);
        product = productRepository.save(product);

        // Update the budget: deduct the product's price from the selected card's budget.
        if (selectedCard != null && currentUser != null) {
            for (UserBudget ub : currentUser.getUserBudgets()) {
                if (ub.getBudget() != null && selectedCard.equals(ub.getBudget().getCardnumber())) {
                    Budget budget = ub.getBudget();
                    double newAmount = budget.getAmount() - price;
                    budget.setAmount(newAmount);
                    budgetRepository.save(budget);
                    System.out.println("Budget updated for card " + selectedCard + ": " + newAmount);
                    break;
                }
            }
        }
        System.out.println("Product added with ID: " + product.getId());
    }

    private void editProduct() {
        Long id = Long.parseLong(IDField.getText());
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            System.out.println("Product not found with ID: " + id);
            return;
        }
        double oldPrice = product.getPrice();

        String selectedCard = cardNoField.getValue();
        String categoryName = categoryField.getValue();
        LocalDate date = dateField.getValue();
        String productName = nameField.getText();
        double newPrice = Double.parseDouble(priceField.getText());

        // Update product details.
        product.setName(productName);
        product.setPrice(newPrice);
        product.setDate(date);

        // Update or create category.
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            category = new Category(categoryName);
            category = categoryRepository.save(category);
            categoryField.getItems().add(category.getName());
        }
        product.setCategory(category);
        product = productRepository.save(product);

        // Adjust the budget: subtract the difference (newPrice - oldPrice).
        double diff = newPrice - oldPrice;
        if (selectedCard != null && currentUser != null) {
            for (UserBudget ub : currentUser.getUserBudgets()) {
                if (ub.getBudget() != null && selectedCard.equals(ub.getBudget().getCardnumber())) {
                    Budget budget = ub.getBudget();
                    double newAmount = budget.getAmount() - diff;
                    budget.setAmount(newAmount);
                    budgetRepository.save(budget);
                    System.out.println("Budget adjusted for card " + selectedCard + ": " + newAmount);
                    break;
                }
            }
        }
        System.out.println("Product updated with ID: " + product.getId());
    }

    private void deleteProduct() {
        Long id = Long.parseLong(IDField.getText());
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            System.out.println("Product not found with ID: " + id);
            return;
        }
        double price = product.getPrice();

        // Delete the product.
        productRepository.delete(product);
        System.out.println("Product deleted with ID: " + id);

        // Restore the budget: add the product's price back to the corresponding budget.
        String selectedCard = cardNoField.getValue();
        if (selectedCard != null && currentUser != null) {
            for (UserBudget ub : currentUser.getUserBudgets()) {
                if (ub.getBudget() != null && selectedCard.equals(ub.getBudget().getCardnumber())) {
                    Budget budget = ub.getBudget();
                    double newAmount = budget.getAmount() + price;
                    budget.setAmount(newAmount);
                    budgetRepository.save(budget);
                    System.out.println("Budget restored for card " + selectedCard + ": " + newAmount);
                    break;
                }
            }
        }
    }

    /**
     * Sets the current user and populates the card number ComboBox.
     *
     * @param user the logged-in user.
     */
    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            cardNoField.getItems().clear();
            if (user.getUserBudgets() != null && !user.getUserBudgets().isEmpty()) {
                Set<String> cardNumbers = new HashSet<>();
                for (UserBudget ub : user.getUserBudgets()) {
                    if (ub.getBudget() != null && ub.getBudget().getCardnumber() != null) {
                        cardNumbers.add(ub.getBudget().getCardnumber());
                    }
                }
                cardNoField.getItems().addAll(cardNumbers);
            }
        } else {
            cardNoField.getItems().clear();
        }
    }
}
