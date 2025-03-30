package com.example.demo.Presentation;

import com.example.demo.Model.Category;
import com.example.demo.Repository.CategoryRepository;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CategoriesControllerTests {

    private CategoriesController controller;
    private CategoryRepository categoryRepository;

    @BeforeAll
    public static void initJavaFX() {
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        categoryRepository = mock(CategoryRepository.class);
        controller = new CategoriesController(categoryRepository);
        ReflectionTestUtils.setField(controller, "AnchorPane", new AnchorPane());
        ReflectionTestUtils.setField(controller, "Form", new Rectangle());
        ReflectionTestUtils.setField(controller, "NameLabel", new Label());
        ReflectionTestUtils.setField(controller, "ScrollPane", new ScrollPane());
        ReflectionTestUtils.setField(controller, "addCategory", new Button());
        ReflectionTestUtils.setField(controller, "homeButton", new Button());
        ReflectionTestUtils.setField(controller, "loggedUsers", new Button());
        ReflectionTestUtils.setField(controller, "manageCategoriesButton", new Button());
        ReflectionTestUtils.setField(controller, "nameField", new TextField());
        ReflectionTestUtils.setField(controller, "saveButton", new Button());
        ReflectionTestUtils.setField(controller, "seeCategories", new Button());
    }

    @Test
    public void testAddCategoryOnAction() {
        Label nameLabel = (Label) ReflectionTestUtils.getField(controller, "NameLabel");
        TextField nameField = (TextField) ReflectionTestUtils.getField(controller, "nameField");
        Button saveButton = (Button) ReflectionTestUtils.getField(controller, "saveButton");
        Rectangle form = (Rectangle) ReflectionTestUtils.getField(controller, "Form");
        nameField.setText("Some text");
        nameLabel.setText("Old Message");
        saveButton.setDisable(true);
        form.setVisible(false);
        controller.addCategoryOnAction(new ActionEvent());
        assertTrue(form.isVisible());
        assertFalse(nameField.isDisabled());
        assertFalse(saveButton.isDisabled());
        assertEquals("", nameField.getText());
        assertEquals("", nameLabel.getText());
    }

    @Test
    public void testSaveOnAction_EmptyName() {
        Label nameLabel = (Label) ReflectionTestUtils.getField(controller, "NameLabel");
        TextField nameField = (TextField) ReflectionTestUtils.getField(controller, "nameField");
        nameField.setText("");
        controller.saveOnAction(new ActionEvent());
        assertEquals("Category name cannot be empty.", nameLabel.getText());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testSaveOnAction_ValidName() {
        Label nameLabel = (Label) ReflectionTestUtils.getField(controller, "NameLabel");
        TextField nameField = (TextField) ReflectionTestUtils.getField(controller, "nameField");
        Rectangle form = (Rectangle) ReflectionTestUtils.getField(controller, "Form");
        nameField.setText("Test Category");
        Category savedCategory = new Category("Test Category");
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        controller.saveOnAction(new ActionEvent());
        assertEquals("Category 'Test Category' saved successfully.", nameLabel.getText());
        assertFalse(form.isVisible());
        verify(categoryRepository, times(1)).save(argThat(category -> "Test Category".equals(category.getName())));
    }

    @Test
    public void testSeeCategoriesOnAction_EmptyList() {
        ScrollPane scrollPane = (ScrollPane) ReflectionTestUtils.getField(controller, "ScrollPane");
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        controller.seeCategoriesOnAction(new ActionEvent());
        Object content = scrollPane.getContent();
        assertNotNull(content);
        assertTrue(content instanceof VBox);
        VBox vbox = (VBox) content;
        assertEquals(1, vbox.getChildren().size());
        Label label = (Label) vbox.getChildren().get(0);
        assertEquals("No categories found.", label.getText());
    }

    @Test
    public void testSeeCategoriesOnAction_NonEmptyList() {
        ScrollPane scrollPane = (ScrollPane) ReflectionTestUtils.getField(controller, "ScrollPane");
        Category category = new Category("Books");
        ReflectionTestUtils.setField(category, "id", 1L);
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        controller.seeCategoriesOnAction(new ActionEvent());
        Object content = scrollPane.getContent();
        assertNotNull(content);
        assertTrue(content instanceof VBox);
        VBox vbox = (VBox) content;
        assertEquals(1, vbox.getChildren().size());
        Label label = (Label) vbox.getChildren().get(0);
        assertEquals("ID: 1 | Name: Books", label.getText());
    }

    @Test
    public void testGoBackOnAction() {
        controller.goBackOnAction(new ActionEvent());
    }
}
