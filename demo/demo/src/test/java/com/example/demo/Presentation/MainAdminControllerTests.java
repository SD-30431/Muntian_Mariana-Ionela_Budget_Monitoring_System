package com.example.demo.Presentation;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MainAdminControllerTests {

    private MainAdminController controller;
    private Button homeButton;
    private Label incomeDisplay;
    private Button loggedUsers;
    private Button manageCategoriesButton;
    private Label usernameDisplay;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        controller = new MainAdminController();
        homeButton = new Button();
        incomeDisplay = new Label();
        loggedUsers = new Button();
        manageCategoriesButton = new Button();
        usernameDisplay = new Label();
        ReflectionTestUtils.setField(controller, "homeButton", homeButton);
        ReflectionTestUtils.setField(controller, "incomeDisplay", incomeDisplay);
        ReflectionTestUtils.setField(controller, "loggedUsers", loggedUsers);
        ReflectionTestUtils.setField(controller, "manageCategoriesButton", manageCategoriesButton);
        ReflectionTestUtils.setField(controller, "usernameDisplay", usernameDisplay);
    }

    @Test
    public void testHomeButtonOnActionThrowsIOException() {
        assertThrows(IOException.class, () -> controller.homeButtonOnAction(new ActionEvent()));
    }

    @Test
    public void testManageCategoriesButtonOnActionThrowsIOException() {
        assertThrows(IOException.class, () -> controller.manageCategoriesButtonOnAction(new ActionEvent()));
    }

    @Test
    public void testLoggedUsersOnActionThrowsIOException() {
        assertThrows(IOException.class, () -> controller.loggedUsersOnAction(new ActionEvent()));
    }

    @Test
    public void testGoBackOnActionDoesNotThrow() {
        assertDoesNotThrow(() -> controller.goBackOnAction(new ActionEvent()));
    }
}
