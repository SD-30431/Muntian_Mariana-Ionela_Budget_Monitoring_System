package com.example.demo.Presentation;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class StartControllerTests {

    private StartController controller;
    private Button createNewAccount;
    private Button loginAdmin;
    private Button loginUser;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX runtime.
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        // Create an instance of JavaFXApplication.
        // Since StartController only uses JavaFXApplication.switchScene (a static method),
        // we can pass any instance.
        JavaFXApplication app = new JavaFXApplication();
        controller = new StartController(app);

        createNewAccount = new Button();
        loginAdmin = new Button();
        loginUser = new Button();

        ReflectionTestUtils.setField(controller, "createNewAccount", createNewAccount);
        ReflectionTestUtils.setField(controller, "loginAdmin", loginAdmin);
        ReflectionTestUtils.setField(controller, "loginUser", loginUser);
    }

    @Test
    public void testCreateNewAccountOnAction() {
        // Call the method and assert that no exception is thrown.
        assertDoesNotThrow(() -> controller.createNewAccountOnAction(new ActionEvent()));
    }

    @Test
    public void testLoginAdminOnAction() {
        assertDoesNotThrow(() -> controller.loginAdminOnAction(new ActionEvent()));
    }

    @Test
    public void testLoginUserOnAction() {
        assertDoesNotThrow(() -> controller.loginUserOnAction(new ActionEvent()));
    }
}
