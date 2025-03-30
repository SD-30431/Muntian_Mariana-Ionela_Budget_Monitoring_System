package com.example.demo.Presentation;

import com.example.demo.Model.User;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JavaFXApplicationTests {

    @BeforeAll
    public static void initJavaFX() {
        // Initialize the JavaFX runtime.
        new JFXPanel();
    }

    @Test
    public void testSetContextAndGetPrimaryStage() {
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        JavaFXApplication.setContext(mockContext);
        // Before start() is called, primaryStage should be null.
        assertNull(JavaFXApplication.getPrimaryStage());
    }

    @Test
    public void testStopClosesContext() {
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        JavaFXApplication.setContext(mockContext);
        JavaFXApplication app = new JavaFXApplication();
        app.stop();
        verify(mockContext, times(1)).close();
    }

    @Test
    public void testStartThrowsWhenContextNotSet() {
        JavaFXApplication.setContext(null);
        JavaFXApplication app = new JavaFXApplication();
        Stage dummyStage = new Stage();
        Exception exception = assertThrows(NullPointerException.class, () -> app.start(dummyStage));
        assertEquals("Spring context is not initialized properly.", exception.getMessage());
    }

    @Test
    public void testSwitchSceneThrowsIOExceptionWhenResourceNotFound() {
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        JavaFXApplication.setContext(mockContext);
        // Expect IOException because the resource does not exist.
        assertThrows(IOException.class, () -> JavaFXApplication.switchScene("NonExistent.fxml"));
    }

    @Test
    public void testSwitchSceneWithUserThrowsIOExceptionWhenResourceNotFound() {
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        JavaFXApplication.setContext(mockContext);
        // Expect IOException because the resource does not exist.
        assertThrows(IOException.class, () -> JavaFXApplication.switchScene("NonExistent.fxml", new User()));
    }
}
