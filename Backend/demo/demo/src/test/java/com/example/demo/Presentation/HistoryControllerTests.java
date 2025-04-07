package com.example.demo.Presentation;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HistoryControllerTests {

    private HistoryController controller;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private AnchorPane anchorPane;
    private ComboBox<String> filterByCategory;
    private DatePicker filterByDate;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        controller = new HistoryController(productRepository, categoryRepository);

        anchorPane = new AnchorPane();
        filterByCategory = new ComboBox<>();
        filterByDate = new DatePicker();

        ReflectionTestUtils.setField(controller, "anchorPane", anchorPane);
        ReflectionTestUtils.setField(controller, "filterByCategory", filterByCategory);
        ReflectionTestUtils.setField(controller, "filterByDate", filterByDate);
    }

    @Test
    public void testInitialize_PopulatesFilterAndDisplaysProducts() {
        Category cat1 = new Category("Electronics");
        Category cat2 = new Category("Books");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        Product product1 = new Product("Laptop", 1000.0, LocalDate.of(2023, 1, 1), cat1);
        ReflectionTestUtils.setField(product1, "id", 1L);
        Product product2 = new Product("Novel", 20.0, LocalDate.of(2023, 1, 2), cat2);
        ReflectionTestUtils.setField(product2, "id", 2L);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        controller.initialize();

        assertEquals("All", filterByCategory.getValue());
        assertTrue(filterByCategory.getItems().contains("Electronics"));
        assertTrue(filterByCategory.getItems().contains("Books"));

        assertEquals(2, anchorPane.getChildren().size());
        List<String> texts = anchorPane.getChildren().stream()
                .filter(n -> n instanceof Text)
                .map(n -> ((Text) n).getText())
                .collect(Collectors.toList());
        assertTrue(texts.get(0).contains("Laptop") || texts.get(1).contains("Laptop"));
        assertTrue(texts.get(0).contains("Novel") || texts.get(1).contains("Novel"));
    }

    @Test
    public void testStartFilterButtonOnAction_FiltersByDate() {
        Category cat1 = new Category("Electronics");
        ReflectionTestUtils.setField(cat1, "id", 1L);
        Category cat2 = new Category("Books");
        ReflectionTestUtils.setField(cat2, "id", 2L);
        Product product1 = new Product("Laptop", 1000.0, LocalDate.of(2023, 1, 1), cat1);
        ReflectionTestUtils.setField(product1, "id", 1L);
        Product product2 = new Product("Novel", 20.0, LocalDate.of(2023, 1, 2), cat2);
        ReflectionTestUtils.setField(product2, "id", 2L);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        filterByCategory.getItems().clear();
        filterByCategory.getItems().add("All");
        filterByCategory.getItems().add("Electronics");
        filterByCategory.getItems().add("Books");
        filterByCategory.setValue("All");

        filterByDate.setValue(LocalDate.of(2023, 1, 1));

        controller.startFilterButtonOnAction(new ActionEvent());

        assertEquals(1, anchorPane.getChildren().size());
        String text = ((Text) anchorPane.getChildren().get(0)).getText();
        assertTrue(text.contains("Laptop"));
    }

    @Test
    public void testStartFilterButtonOnAction_FiltersByCategory() {
        Category cat1 = new Category("Electronics");
        ReflectionTestUtils.setField(cat1, "id", 1L);
        Category cat2 = new Category("Books");
        ReflectionTestUtils.setField(cat2, "id", 2L);
        Product product1 = new Product("Laptop", 1000.0, LocalDate.of(2023, 1, 1), cat1);
        ReflectionTestUtils.setField(product1, "id", 1L);
        Product product2 = new Product("Novel", 20.0, LocalDate.of(2023, 1, 2), cat2);
        ReflectionTestUtils.setField(product2, "id", 2L);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        filterByCategory.getItems().clear();
        filterByCategory.getItems().add("All");
        filterByCategory.getItems().add("Electronics");
        filterByCategory.getItems().add("Books");
        filterByCategory.setValue("Books");
        filterByDate.setValue(null);

        controller.startFilterButtonOnAction(new ActionEvent());

        assertEquals(1, anchorPane.getChildren().size());
        String text = ((Text) anchorPane.getChildren().get(0)).getText();
        assertTrue(text.contains("Novel"));
    }

    @Test
    public void testNavigationMethodsThrowIOException() {
        ActionEvent event = new ActionEvent();
        assertThrows(IOException.class, () -> controller.chartExpensesButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.goBackOnAction(event));
        assertThrows(IOException.class, () -> controller.historyButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.homeButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.manageCardsButtonOnAction(event));
        assertThrows(IOException.class, () -> controller.manageExpensesOnAction(event));
    }
}
