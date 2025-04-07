package com.example.demo.Presentation;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MainControllerTests {

    private MainController controller;
    private Label cardsDisplay;
    private Label cardsOwn;
    private Button chartExpensesButton;
    private Button historyButton;
    private Button homeButton;
    private Label incomeDisplay;
    private Button manageCardsButton;
    private Button manageExpenses;
    private Label usernameDisplay;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        controller = new MainController();
        cardsDisplay = new Label();
        cardsOwn = new Label();
        chartExpensesButton = new Button();
        historyButton = new Button();
        homeButton = new Button();
        incomeDisplay = new Label();
        manageCardsButton = new Button();
        manageExpenses = new Button();
        usernameDisplay = new Label();

        ReflectionTestUtils.setField(controller, "cardsDisplay", cardsDisplay);
        ReflectionTestUtils.setField(controller, "cardsOwn", cardsOwn);
        ReflectionTestUtils.setField(controller, "chartExpensesButton", chartExpensesButton);
        ReflectionTestUtils.setField(controller, "historyButton", historyButton);
        ReflectionTestUtils.setField(controller, "homeButton", homeButton);
        ReflectionTestUtils.setField(controller, "incomeDisplay", incomeDisplay);
        ReflectionTestUtils.setField(controller, "manageCardsButton", manageCardsButton);
        ReflectionTestUtils.setField(controller, "manageExpenses", manageExpenses);
        ReflectionTestUtils.setField(controller, "usernameDisplay", usernameDisplay);

        // Call initialize() to set default texts.
        controller.initialize();
    }

    @Test
    public void testInitialize() {
        assertEquals("User: N/A", usernameDisplay.getText());
        assertEquals("Income: N/A", incomeDisplay.getText());
        assertEquals("", cardsOwn.getText());
        assertEquals("", cardsDisplay.getText());
    }

    @Test
    public void testSetUser_NoBudgets() {
        User user = new User();
        user.setUsername("JohnDoe");
        user.setSalary(5000.0);
        user.setUserBudgets(Collections.emptySet());
        controller.setUser(user);
        assertEquals("JohnDoe", usernameDisplay.getText());
        assertEquals("5000.0", incomeDisplay.getText());
        assertEquals("No Cards Owned", cardsOwn.getText());
        assertEquals("", cardsDisplay.getText());
    }

    @Test
    public void testSetUser_WithBudgets() {
        User user = new User();
        user.setUsername("JohnDoe");
        user.setSalary(5000.0);
        Budget budget = new Budget(1000.0, "1234-5678-9012-3456");
        UserBudget userBudget = new UserBudget(user, budget);
        Set<UserBudget> budgets = new HashSet<>();
        budgets.add(userBudget);
        user.setUserBudgets(budgets);
        controller.setUser(user);
        assertEquals("JohnDoe", usernameDisplay.getText());
        assertEquals("5000.0", incomeDisplay.getText());
        assertEquals("Cards Owned:", cardsOwn.getText());
        String expectedDisplay = "• Card Number: 1234-5678-9012-3456 | Budget: 1000.0\n";
        assertEquals(expectedDisplay, cardsDisplay.getText());
    }

    @Test
    public void testChartExpensesButtonOnActionThrowsIOException() {
        // Assuming JavaFXApplication.switchScene throws IOException in the test environment.
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.chartExpensesButtonOnAction(event));
    }

    @Test
    public void testHistoryButtonOnActionThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.historyButtonOnAction(event));
    }

    @Test
    public void testHomeButtonOnActionThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.homeButtonOnAction(event));
    }

    @Test
    public void testManageCardsButtonOnActionThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.manageCardsButtonOnAction(event));
    }

    @Test
    public void testManageExpensesOnActionThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.manageExpensesOnAction(event));
    }
}
