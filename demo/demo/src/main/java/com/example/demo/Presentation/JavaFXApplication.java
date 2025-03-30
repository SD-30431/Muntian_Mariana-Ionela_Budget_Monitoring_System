package com.example.demo.Presentation;

import com.example.demo.Model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JavaFXApplication extends Application {

    private static ConfigurableApplicationContext context;
    private static Stage primaryStage;

    public static void setContext(ConfigurableApplicationContext context) {
        JavaFXApplication.context = context;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (context == null) {
            throw new NullPointerException("Spring context is not initialized properly.");
        }
        primaryStage = stage;

        switchScene("StartPage.fxml");

        primaryStage.setTitle("Budget Monitoring");
        primaryStage.show();
    }

    /**
     * Switches scene without a user.
     *
     * @param fxmlFile The FXML file to load.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void switchScene(String fxmlFile) throws IOException {
        switchScene(fxmlFile, null);
    }

    /**
     * Switches scene and passes the current user to the controller (if applicable).
     *
     * @param fxmlFile    The FXML file to load.
     * @param currentUser The user object to pass; can be null.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void switchScene(String fxmlFile, User currentUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(JavaFXApplication.class.getResource("/" + fxmlFile));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Object controller = loader.getController();
        if (controller instanceof MainController && currentUser != null) {
            ((MainController) controller).setUser(currentUser);
        }
        if (controller instanceof ManageCards && currentUser != null) {
            ((ManageCards) controller).setUser(currentUser);
        }
        if (controller instanceof ManageExpenses && currentUser != null) {
            ((ManageExpenses) controller).setUser(currentUser);
        }
        if (controller instanceof ChartController && currentUser != null) {
            ((ChartController) controller).setUser(currentUser);
        }
        if (controller instanceof HistoryController && currentUser != null) {
            ((HistoryController) controller).setUser(currentUser);
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (context != null) {
            context.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
