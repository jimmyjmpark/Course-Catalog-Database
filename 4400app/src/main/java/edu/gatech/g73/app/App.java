package edu.gatech.g73.app;

import edu.gatech.g73.controller.FXController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

/**
 * App is responsible for starting the application.
 * It is also responsible for rendering Scenes from FXML and caller-supplied initial data.
 *
 * @author Andrew Ray
 * @since 12/1/2016
 * @version 0.1.0
 * Created by someone on 12/1/16.
 */
public class App extends Application {
    // http://stackoverflow.com/questions/13003323/javafx-how-to-change-stage/13003602#13003602
    
    public final int DEFAULT_STAGE_WIDTH = 1280;
    public final int DEFAULT_STAGE_HEIGHT = 720;
    
    private Stage stage;
    private static App                 instance;

    public App() {
        instance = this;
    }

    /**
     * Returns the only instance of this class
     *
     * @return Singleton App instance
     */
    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    /** Starts this application */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Required for JavaFX to start rendering a Window and its contents
     *
     * @param stage Window to display content in
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        try {
            stage.setTitle("Project Explorer");
            showScene("/login.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders new window contents, provided from a given FXML file, and any initial values.
     *
     * @param fxmlLocation Location of FXML file to render
     * @param initVals Potentially any initial values to pass into the Scene's controller
     * @throws IOException Thrown when fxmlLocation is invalid
     */
    public void showScene(String fxmlLocation, Map<?, ?> initVals) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlLocation));
        Parent page = loader.load();
        FXController controller = loader.getController();
        if (initVals != null) {
            controller.initData(initVals);
        }
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, DEFAULT_STAGE_WIDTH, DEFAULT_STAGE_HEIGHT);
            // scene.getStylesheets().add(App.class.getResource("default.css").toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        stage.show();
    }

}
