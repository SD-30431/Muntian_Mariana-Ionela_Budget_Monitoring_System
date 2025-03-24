package com.example.demo.Presentation;

import com.example.demo.Model.Category;
import com.example.demo.Repository.CategoryRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CategoriesController {

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Rectangle Form;

    @FXML
    private Label NameLabel;

    @FXML
    private ScrollPane ScrollPane;

    @FXML
    private Button addCategory;

    @FXML
    private Button homeButton;

    @FXML
    private Button loggedUsers;

    @FXML
    private Button manageCategoriesButton;

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    @FXML
    private Button seeCategories;

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * When the user clicks "Add Category", reveal the form
     * (clear and enable the nameField and saveButton) so that the user can enter a new category name.
     */
    @FXML
    void addCategoryOnAction(ActionEvent event) {
        Form.setVisible(true);
        nameField.setDisable(false);
        saveButton.setDisable(false);
        nameField.clear();
        NameLabel.setText("");  // Clear any previous messages.
    }

    /**
     * Switches the scene to the main admin page.
     */
    @FXML
    void homeButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("MainAdminPage.fxml");
    }

    /**
     * Switches the scene to the logged users page.
     */
    @FXML
    void loggedUsersOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("LoggedUsersPage.fxml");
    }

    /**
     * Refreshes the current categories view by switching to the CategoriesPage scene.
     */
    @FXML
    void manageCategoriesButtonOnAction(ActionEvent event) throws IOException {
        JavaFXApplication.switchScene("CategoriesPage.fxml");
    }

    /**
     * Saves a new category. Reads the category name from nameField,
     * validates it, saves it via the repository, and then displays a confirmation message.
     * Finally, the form is hidden.
     */
    @FXML
    void saveOnAction(ActionEvent event) {
        String categoryName = nameField.getText().trim();
        if (categoryName.isEmpty()) {
            NameLabel.setText("Category name cannot be empty.");
            return;
        }
        Category category = new Category(categoryName);
        categoryRepository.save(category);
        NameLabel.setText("Category '" + categoryName + "' saved successfully.");
        // Hide the form after saving.
        Form.setVisible(false);
    }

    /**
     * Retrieves all categories from the database, creates a VBox containing a label
     * for each category, and sets the VBox as the content of the ScrollPane.
     */
    @FXML
    void seeCategoriesOnAction(ActionEvent event) {
        List<Category> categories = categoryRepository.findAll();
        VBox vbox = new VBox(5);  // 5px spacing between items
        if (categories.isEmpty()) {
            vbox.getChildren().add(new Label("No categories found."));
        } else {
            for (Category cat : categories) {
                Label label = new Label("ID: " + cat.getId() + " | Name: " + cat.getName());
                vbox.getChildren().add(label);
            }
        }
        ScrollPane.setContent(vbox);
    }
    @FXML
    void goBackOnAction(ActionEvent event) {
        try {
            JavaFXApplication.switchScene("StartPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
