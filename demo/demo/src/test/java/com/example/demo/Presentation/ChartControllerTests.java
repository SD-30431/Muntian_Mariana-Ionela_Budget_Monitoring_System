package com.example.demo.Presentation;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Repository.ProductRepository;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChartControllerTests {

    private ChartController controller;
    private ProductRepository productRepository;
    private PieChart expensesChart;
    private Button chartExpensesButton;
    private Button goBack;
    private Button historyButton;
    private Button homeButton;
    private Button manageCardsButton;
    private Button manageExpenses;

    @BeforeAll
    public static void initJavaFX() {
        // Initializes JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        controller = new ChartController(productRepository);

        // Initialize FXML-injected fields.
        expensesChart = new PieChart();
        ReflectionTestUtils.setField(controller, "expensesChart", expensesChart);
        chartExpensesButton = new Button();
        ReflectionTestUtils.setField(controller, "chartExpensesButton", chartExpensesButton);
        goBack = new Button();
        ReflectionTestUtils.setField(controller, "goBack", goBack);
        historyButton = new Button();
        ReflectionTestUtils.setField(controller, "historyButton", historyButton);
        homeButton = new Button();
        ReflectionTestUtils.setField(controller, "homeButton", homeButton);
        manageCardsButton = new Button();
        ReflectionTestUtils.setField(controller, "manageCardsButton", manageCardsButton);
        manageExpenses = new Button();
        ReflectionTestUtils.setField(controller, "manageExpenses", manageExpenses);
    }

    @Test
    public void testInitializeClearsChartData() {
        expensesChart.getData().add(new PieChart.Data("Test", 100));
        controller.initialize();
        assertTrue(expensesChart.getData().isEmpty(), "Chart data should be cleared after initialization.");
    }

    @Test
    public void testChartExpensesButtonOnActionUpdatesChart() {
        // Prepare sample categories and products.
        Category category1 = new Category("Food");
        ReflectionTestUtils.setField(category1, "id", 1L);
        Category category2 = new Category("Transport");
        ReflectionTestUtils.setField(category2, "id", 2L);

        Product product1 = new Product("Apple", 100.0, LocalDate.now(), category1);
        Product product2 = new Product("Bus Ticket", 50.0, LocalDate.now(), category2);
        Product product3 = new Product("Water", 25.0, LocalDate.now(), null); // Uncategorized

        List<Product> products = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(products);

        // Invoke the method that updates the chart.
        controller.chartExpensesButtonOnAction(new ActionEvent());

        // Total spending = 100 + 50 + 25 = 175.
        // Expected percentages:
        // Food: (100/175)*100 = 57.1%
        // Transport: (50/175)*100 = 28.6%
        // Uncategorized: (25/175)*100 = 14.3%
        assertEquals(3, expensesChart.getData().size(), "There should be three slices in the chart.");

        for (PieChart.Data data : expensesChart.getData()) {
            String name = data.getName();
            if (name.startsWith("Food")) {
                assertEquals(100.0, data.getPieValue());
                assertTrue(name.contains("57.1"), "Food slice label should include 57.1%.");
            } else if (name.startsWith("Transport")) {
                assertEquals(50.0, data.getPieValue());
                assertTrue(name.contains("28.6"), "Transport slice label should include 28.6%.");
            } else if (name.startsWith("Uncategorized")) {
                assertEquals(25.0, data.getPieValue());
                assertTrue(name.contains("14.3"), "Uncategorized slice label should include 14.3%.");
            } else {
                fail("Unexpected slice label: " + name);
            }
        }
    }

    @Test
    public void testSetUserUpdatesChart() {
        // Prepare a sample product.
        Category category = new Category("Entertainment");
        ReflectionTestUtils.setField(category, "id", 3L);
        Product product = new Product("Movie", 20.0, LocalDate.now(), category);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // Set a user, which triggers updateChart.
        User user = new User();
        user.setUsername("testUser");
        controller.setUser(user);

        assertEquals(1, expensesChart.getData().size(), "Chart should have one slice.");
        PieChart.Data slice = expensesChart.getData().get(0);
        assertTrue(slice.getName().startsWith("Entertainment"), "Slice label should start with the category name.");
        assertEquals(20.0, slice.getPieValue());
    }

    // For navigation methods, we test that no exception is thrown.
    @Test
    public void testGoBackOnActionDoesNotThrow() {
        try {
            controller.goBackOnAction(new ActionEvent());
        } catch (IOException e) {
            fail("goBackOnAction should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testHistoryButtonOnActionDoesNotThrow() {
        try {
            controller.historyButtonOnAction(new ActionEvent());
        } catch (IOException e) {
            fail("historyButtonOnAction should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testHomeButtonOnActionDoesNotThrow() {
        try {
            controller.homeButtonOnAction(new ActionEvent());
        } catch (IOException e) {
            fail("homeButtonOnAction should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testManageCardsButtonOnActionDoesNotThrow() {
        try {
            controller.manageCardsButtonOnAction(new ActionEvent());
        } catch (IOException e) {
            fail("manageCardsButtonOnAction should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testManageExpensesOnActionDoesNotThrow() {
        try {
            controller.manageExpensesOnAction(new ActionEvent());
        } catch (IOException e) {
            fail("manageExpensesOnAction should not throw an exception: " + e.getMessage());
        }
    }
}
