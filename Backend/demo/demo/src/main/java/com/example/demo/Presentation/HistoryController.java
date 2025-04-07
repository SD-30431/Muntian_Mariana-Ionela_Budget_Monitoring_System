package com.example.demo.Presentation;

import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button chartExpensesButton;

    @FXML
    private ComboBox<String> filterByCategory;

    @FXML
    private DatePicker filterByDate;

    @FXML
    private Button goBack;

    @FXML
    private Button historyButton1;

    @FXML
    private Button homeButton;

    @FXML
    private Button manageCardsButton;

    @FXML
    private Button manageExpenses;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button startFilterButton;

    private User currentUser;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public HistoryController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * This method is called automatically when the FXML is loaded.
     * It populates the filterByCategory ComboBox and displays all products initially.
     */
    @FXML
    public void initialize() {
        filterByCategory.getItems().clear();
        filterByCategory.getItems().add("All");
        categoryRepository.findAll().forEach(cat -> filterByCategory.getItems().add(cat.getName()));
        filterByCategory.setValue("All");

        List<Product> allProducts = productRepository.findAll();
        updateProductsDisplay(allProducts);
    }

    /**
     * Sets the current user.
     *
     * @param user the logged-in user.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Updates the anchorPane (inside the scrollPane) to display the list of products.
     *
     * @param products the list of products to display.
     */
    private void updateProductsDisplay(List<Product> products) {
        anchorPane.getChildren().clear();
        double yOffset = 10;
        for (Product p : products) {
            String info = String.format("ID: %d | Name: %s | Price: %.2f | Date: %s | Category: %s",
                    p.getId(), p.getName(), p.getPrice(), p.getDate(), p.getCategory().getName());
            Text text = new Text(info);
            text.setLayoutX(10);
            text.setLayoutY(yOffset);
            yOffset += 25;
            anchorPane.getChildren().add(text);
        }
    }

    /**
     * Called when the "Start Filter" button is pressed.
     * Filters products based on the selected category and/or date.
     */
    @FXML
    void startFilterButtonOnAction(ActionEvent event) {
        String selectedCategory = filterByCategory.getValue();
        LocalDate selectedDate = filterByDate.getValue(); // Ensure your FXML defines a DatePicker with fx:id="filterByDate"

        List<Product> allProducts = productRepository.findAll();
        List<Product> filteredProducts = allProducts.stream()
                .filter(p -> {
                    // If category filter is not "All", check category match.
                    if (!"All".equals(selectedCategory)) {
                        return p.getCategory().getName().equals(selectedCategory);
                    }
                    return true;
                })
                .filter(p -> {
                    // If a date is selected, filter by date.
                    if (selectedDate != null) {
                        return p.getDate().equals(selectedDate);
                    }
                    return true;
                })
                .collect(Collectors.toList());
        updateProductsDisplay(filteredProducts);
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
        JavaFXApplication.switchScene("ManageCardsPage.fxml", currentUser);
    }

    @FXML
    void manageExpensesOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("ManageExpenses.fxml", currentUser);
    }
}
