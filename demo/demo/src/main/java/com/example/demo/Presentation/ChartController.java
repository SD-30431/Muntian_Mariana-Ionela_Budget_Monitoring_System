package com.example.demo.Presentation;

import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Repository.ProductRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChartController {

    @FXML
    private Button chartExpensesButton;

    @FXML
    private PieChart expensesChart;

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

    // The current logged-in user.
    private User currentUser;

    // Inject the ProductRepository.
    private final ProductRepository productRepository;

    public ChartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @FXML
    public void initialize() {
        expensesChart.getData().clear();
    }

    /**
     * Navigation methods.
     */
    @FXML
    void chartExpensesButtonOnAction(ActionEvent event) {
        updateChart();
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

    /**
     * Called by the scene-switching mechanism to pass the current user.
     *
     * @param currentUser the logged-in user.
     */
    public void setUser(User currentUser) {
        this.currentUser = currentUser;
        updateChart();
    }

    /**
     * Loads all products for the current user, sums up the spending per category,
     * calculates each category's percentage of total spending,
     * and updates the PieChart.
     */
    private void updateChart() {

        List<Product> products = productRepository.findAll();

        // Sum prices per category name (use "Uncategorized" if a product has no category).
        Map<String, Double> totalByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory() != null ? p.getCategory().getName() : "Uncategorized",
                        Collectors.summingDouble(Product::getPrice)
                ));

        // Compute total spending.
        double totalSpending = totalByCategory.values().stream().mapToDouble(Double::doubleValue).sum();

        // Clear previous chart data.
        expensesChart.getData().clear();

        // For each category, calculate its percentage and add a slice to the chart.
        totalByCategory.forEach((category, amount) -> {
            double percentage = (totalSpending > 0) ? (amount / totalSpending) * 100 : 0;
            String label = category + " (" + String.format("%.1f", percentage) + "%)";
            PieChart.Data slice = new PieChart.Data(label, amount);
            expensesChart.getData().add(slice);
        });
    }
}
