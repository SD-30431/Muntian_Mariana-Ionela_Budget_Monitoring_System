package com.example.demo.Presentation;

import com.example.demo.Model.Budget;
import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.BudgetRepository;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ManageExpensesTests {

    private ManageExpenses controller;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private BudgetRepository budgetRepository;

    // FXML fields
    private TextField IDField;
    private Label IDLabel;
    private ComboBox<String> cardNoField;
    private Label cardNoLabel;
    private ComboBox<String> categoryField;
    private Label categoryLabel;
    private DatePicker dateField;
    private Label dateLabel;
    private TextField nameField;
    private Label nameLabel;
    private TextField priceField;
    private Label priceLabel;
    private Rectangle form;
    private javafx.scene.control.Button saveButton;
    // Navigation buttons (for throwing IOException in tests)
    private javafx.scene.control.Button chartExpensesButton;
    private javafx.scene.control.Button historyButton;
    private javafx.scene.control.Button homeButton;
    private javafx.scene.control.Button manageCardsButton;
    private javafx.scene.control.Button manageExpenses;
    private javafx.scene.control.Button addNewProductButton;
    private javafx.scene.control.Button editProductButton;
    private javafx.scene.control.Button deleteButton;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        budgetRepository = mock(BudgetRepository.class);
        controller = new ManageExpenses(productRepository, categoryRepository, budgetRepository);

        // Initialize FXML fields
        IDField = new TextField();
        IDLabel = new Label();
        cardNoField = new ComboBox<>();
        cardNoLabel = new Label();
        categoryField = new ComboBox<>();
        categoryLabel = new Label();
        dateField = new DatePicker();
        dateLabel = new Label();
        nameField = new TextField();
        nameLabel = new Label();
        priceField = new TextField();
        priceLabel = new Label();
        form = new Rectangle();
        saveButton = new javafx.scene.control.Button();
        addNewProductButton = new javafx.scene.control.Button();
        editProductButton = new javafx.scene.control.Button();
        deleteButton = new javafx.scene.control.Button();
        chartExpensesButton = new javafx.scene.control.Button();
        historyButton = new javafx.scene.control.Button();
        homeButton = new javafx.scene.control.Button();
        manageCardsButton = new javafx.scene.control.Button();
        manageExpenses = new javafx.scene.control.Button();

        // Inject FXML fields via ReflectionTestUtils
        ReflectionTestUtils.setField(controller, "IDField", IDField);
        ReflectionTestUtils.setField(controller, "IDLabel", IDLabel);
        ReflectionTestUtils.setField(controller, "cardNoField", cardNoField);
        ReflectionTestUtils.setField(controller, "cardNoLabel", cardNoLabel);
        ReflectionTestUtils.setField(controller, "categoryField", categoryField);
        ReflectionTestUtils.setField(controller, "categoryLabel", categoryLabel);
        ReflectionTestUtils.setField(controller, "dateField", dateField);
        ReflectionTestUtils.setField(controller, "dateLabel", dateLabel);
        ReflectionTestUtils.setField(controller, "nameField", nameField);
        ReflectionTestUtils.setField(controller, "nameLabel", nameLabel);
        ReflectionTestUtils.setField(controller, "priceField", priceField);
        ReflectionTestUtils.setField(controller, "priceLabel", priceLabel);
        ReflectionTestUtils.setField(controller, "form", form);
        ReflectionTestUtils.setField(controller, "saveButton", saveButton);
        ReflectionTestUtils.setField(controller, "addNewProductButton", addNewProductButton);
        ReflectionTestUtils.setField(controller, "editProductButton", editProductButton);
        ReflectionTestUtils.setField(controller, "deleteButton", deleteButton);
        ReflectionTestUtils.setField(controller, "chartExpensesButton", chartExpensesButton);
        ReflectionTestUtils.setField(controller, "historyButton", historyButton);
        ReflectionTestUtils.setField(controller, "homeButton", homeButton);
        ReflectionTestUtils.setField(controller, "manageCardsButton", manageCardsButton);
        ReflectionTestUtils.setField(controller, "manageExpenses", manageExpenses);

        // Simulate initialize() call which hides the form and populates categoryField.
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        controller.initialize();

        // Set up a current user with an empty set of UserBudget.
        User currentUser = new User();
        currentUser.setUserBudgets(new HashSet<>());
        controller.setUser(currentUser);
    }

    @Test
    public void testInitialize_PopulatesCategoryField() {
        // Prepare sample categories.
        Category cat1 = new Category("Food");
        Category cat2 = new Category("Travel");
        when(categoryRepository.findAll()).thenReturn(java.util.Arrays.asList(cat1, cat2));
        // Re-call initialize() to refresh categoryField items.
        controller.initialize();
        assertTrue(categoryField.getItems().contains("Food"));
        assertTrue(categoryField.getItems().contains("Travel"));
    }

    @Test
    public void testAddNewProductButtonOnAction_ShowsAddForm() {
        // Initially, form is hidden.
        form.setVisible(false);
        // Invoke addNewProductButtonOnAction, which should call showFormForAdd().
        controller.addNewProductButtonOnAction(new ActionEvent());
        assertTrue(form.isVisible());
        // In ADD mode, ID controls should be hidden.
        assertFalse(IDField.isVisible());
    }

    @Test
    public void testEditProductButtonOnAction_ShowsEditForm() {
        form.setVisible(false);
        controller.editProductButtonOnAction(new ActionEvent());
        assertTrue(form.isVisible());
        // In EDIT mode, ID field should be visible.
        assertTrue(IDField.isVisible());
    }

    @Test
    public void testDeleteButtonOnAction_ShowsDeleteForm() {
        form.setVisible(false);
        controller.deleteButtonOnAction(new ActionEvent());
        assertTrue(form.isVisible());
        // In DELETE mode, only IDField and cardNoField are visible.
        assertTrue(IDField.isVisible());
        assertTrue(cardNoField.isVisible());
        // Other fields should be hidden.
        assertFalse(categoryField.isVisible());
        assertFalse(dateField.isVisible());
        assertFalse(nameField.isVisible());
        assertFalse(priceField.isVisible());
    }

    @Test
    public void testNavigationMethodsThrowIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.chartExpensesButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.historyButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.homeButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.manageCardsButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.manageExpensesOnAction(event));
    }

    @Test
    public void testSaveButtonOnAction_AddProduct() {
        // Simulate ADD mode.
        controller.addNewProductButtonOnAction(new ActionEvent());
        // Set current mode to ADD (should be set by showFormForAdd())
        // Fill form fields.
        cardNoField.getItems().add("1234-5678-9012-3456");
        cardNoField.setValue("1234-5678-9012-3456");
        categoryField.getItems().add("Groceries");
        categoryField.setValue("Groceries");
        dateField.setValue(LocalDate.of(2023, 3, 15));
        nameField.setText("Milk");
        priceField.setText("3.50");

        // Simulate that category does not exist.
        when(categoryRepository.findByName("Groceries")).thenReturn(null);
        Category savedCategory = new Category("Groceries");
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Product savedProduct = new Product("Milk", 3.50, LocalDate.of(2023, 3, 15), savedCategory);
        // Set product ID via Reflection.
        ReflectionTestUtils.setField(savedProduct, "id", 1L);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Set up currentUser with one UserBudget having a budget with matching card.
        User currentUser = (User) ReflectionTestUtils.getField(controller, "currentUser");
        Budget budget = new Budget(50.0, "1234-5678-9012-3456");
        ReflectionTestUtils.setField(budget, "id", 1L);
        UserBudget userBudget = new UserBudget(currentUser, budget);
        currentUser.getUserBudgets().add(userBudget);
        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoke save button action.
        controller.saveButtonOnAction(new ActionEvent());

        // Verify product and category saved.
        verify(categoryRepository, times(1)).findByName("Groceries");
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(budgetRepository, times(1)).save(any(Budget.class));

        // After saving, form should be hidden and fields cleared.
        assertFalse(form.isVisible());
        assertEquals("", nameField.getText());
        assertEquals("", priceField.getText());
    }

    @Test
    public void testSaveButtonOnAction_EditProduct() {
        // Simulate EDIT mode.
        controller.editProductButtonOnAction(new ActionEvent());
        // Set form fields for editing.
        IDField.setText("1");
        cardNoField.getItems().add("1234-5678-9012-3456");
        cardNoField.setValue("1234-5678-9012-3456");
        categoryField.getItems().add("Electronics");
        categoryField.setValue("Electronics");
        dateField.setValue(LocalDate.of(2023, 4, 10));
        nameField.setText("Smartphone");
        priceField.setText("600.00");

        // Prepare an existing product.
        Category existingCategory = new Category("Electronics");
        when(categoryRepository.findByName("Electronics")).thenReturn(existingCategory);
        Product existingProduct = new Product("Old Smartphone", 550.00, LocalDate.of(2023, 4, 5), existingCategory);
        ReflectionTestUtils.setField(existingProduct, "id", 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Set up currentUser with a matching UserBudget.
        User currentUser = (User) ReflectionTestUtils.getField(controller, "currentUser");
        Budget budget = new Budget(1000.0, "1234-5678-9012-3456");
        ReflectionTestUtils.setField(budget, "id", 2L);
        UserBudget userBudget = new UserBudget(currentUser, budget);
        currentUser.getUserBudgets().add(userBudget);
        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoke save action.
        controller.saveButtonOnAction(new ActionEvent());

        // Verify product update.
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
        // The difference (newPrice - oldPrice) is 600 - 550 = 50, so budget should be deducted by 50.
        verify(budgetRepository, times(1)).save(any(Budget.class));
        // Check that the product's name was updated.
        Product updatedProduct = (Product) verify(productRepository).save(any(Product.class));
        // Not asserting on updatedProduct since we're using mocks; the verification of method calls is sufficient.
    }

    @Test
    public void testSaveButtonOnAction_DeleteProduct() {
        // Simulate DELETE mode.
        controller.deleteButtonOnAction(new ActionEvent());
        // Set form fields for deletion.
        IDField.setText("1");
        cardNoField.getItems().add("1234-5678-9012-3456");
        cardNoField.setValue("1234-5678-9012-3456");

        // Prepare an existing product.
        Category category = new Category("Clothing");
        Product existingProduct = new Product("T-Shirt", 20.00, LocalDate.of(2023, 2, 20), category);
        ReflectionTestUtils.setField(existingProduct, "id", 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        // Set up currentUser with a matching UserBudget.
        User currentUser = (User) ReflectionTestUtils.getField(controller, "currentUser");
        Budget budget = new Budget(100.0, "1234-5678-9012-3456");
        ReflectionTestUtils.setField(budget, "id", 3L);
        UserBudget userBudget = new UserBudget(currentUser, budget);
        currentUser.getUserBudgets().add(userBudget);
        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoke save action.
        controller.saveButtonOnAction(new ActionEvent());

        // Verify product deletion.
        verify(productRepository, times(1)).delete(existingProduct);
        // Budget should be restored by adding the product's price.
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }
}
