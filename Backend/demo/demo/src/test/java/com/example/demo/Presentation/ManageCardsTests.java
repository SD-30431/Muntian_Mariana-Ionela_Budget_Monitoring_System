package com.example.demo.Presentation;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.BudgetRepository;
import com.example.demo.Repository.UserBudgetRepository;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ManageCardsTests {

    private ManageCards controller;
    private BudgetRepository budgetRepository;
    private UserBudgetRepository userBudgetRepository;

    // FXML fields
    private Rectangle form;
    private TextField amountField;
    private TextField cardNoField;
    private Button AddNewCard;
    private Button chartExpensesButton;
    private Button goBack;
    private Button historyButton;
    private Button homeButton;
    private Button manageCardsButton;
    private Button manageExpenses;
    private Button saveButton;

    @BeforeAll
    public static void initJavaFX() {
        new JFXPanel(); // Initializes JavaFX environment
    }

    @BeforeEach
    public void setup() {
        budgetRepository = mock(BudgetRepository.class);
        userBudgetRepository = mock(UserBudgetRepository.class);
        controller = new ManageCards(budgetRepository, userBudgetRepository);

        form = new Rectangle();
        amountField = new TextField();
        cardNoField = new TextField();
        AddNewCard = new Button();
        chartExpensesButton = new Button();
        goBack = new Button();
        historyButton = new Button();
        homeButton = new Button();
        manageCardsButton = new Button();
        manageExpenses = new Button();
        saveButton = new Button();

        ReflectionTestUtils.setField(controller, "form", form);
        ReflectionTestUtils.setField(controller, "amountField", amountField);
        ReflectionTestUtils.setField(controller, "cardNoField", cardNoField);
        ReflectionTestUtils.setField(controller, "AddNewCard", AddNewCard);
        ReflectionTestUtils.setField(controller, "chartExpensesButton", chartExpensesButton);
        ReflectionTestUtils.setField(controller, "goBack", goBack);
        ReflectionTestUtils.setField(controller, "historyButton", historyButton);
        ReflectionTestUtils.setField(controller, "homeButton", homeButton);
        ReflectionTestUtils.setField(controller, "manageCardsButton", manageCardsButton);
        ReflectionTestUtils.setField(controller, "manageExpenses", manageExpenses);
        ReflectionTestUtils.setField(controller, "saveButton", saveButton);

        // Simulate initialize() call
        controller.initialize();

        // Set a current user for tests that require it.
        User currentUser = new User();
        currentUser.setUserBudgets(new java.util.HashSet<>());
        controller.setUser(currentUser);
    }

    @Test
    public void testInitialize_HidesForm() {
        // In initialize(), form.setVisible(false) is called.
        assertFalse(form.isVisible());
    }

    @Test
    public void testAddNewCardOnAction_ShowsForm() {
        form.setVisible(false);
        controller.AddNewCardOnAction(new ActionEvent());
        assertTrue(form.isVisible());
    }

    @Test
    public void testChartExpensesButtonOnAction_ThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.chartExpensesButtonOnAction(event));
    }

    @Test
    public void testGoBackOnAction_ThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.goBackOnAction(event));
    }

    @Test
    public void testHistoryButtonOnAction_ThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.historyButtonOnAction(event));
    }

    @Test
    public void testHomeButtonOnAction_ThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.homeButtonOnAction(event));
    }

    @Test
    public void testManageCardsButtonOnAction_ThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.manageCardsButtonOnAction(event));
    }

    @Test
    public void testManageExpensesOnAction_ThrowsIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.manageExpensesOnAction(event));
    }

    @Test
    public void testSaveButtonOnAction_ValidInput() {
        String amountText = "500.0";
        String cardNumber = "1111-2222-3333-4444";
        amountField.setText(amountText);
        cardNoField.setText(cardNumber);

        Budget savedBudget = new Budget(500.0, cardNumber);
        when(budgetRepository.save(any(Budget.class))).thenReturn(savedBudget);
        User currentUser = (User) ReflectionTestUtils.getField(controller, "currentUser");
        UserBudget savedUserBudget = new UserBudget(currentUser, savedBudget);
        when(userBudgetRepository.save(any())).thenReturn(savedUserBudget);

        controller.saveButtonOnAction(new ActionEvent());

        verify(budgetRepository, times(1)).save(any(Budget.class));
        verify(userBudgetRepository, times(1)).save(any());
        assertFalse(form.isVisible());
        assertEquals("", amountField.getText());
        assertEquals("", cardNoField.getText());
    }

    @Test
    public void testSaveButtonOnAction_InvalidInput() {
        // Test with empty fields
        amountField.setText("");
        cardNoField.setText("");
        controller.saveButtonOnAction(new ActionEvent());
        verify(budgetRepository, never()).save(any(Budget.class));
        verify(userBudgetRepository, never()).save(any());
    }
}
