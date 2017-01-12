package edu.gatech.g73.controller;

import com.jfoenix.controls.JFXButton;
import edu.gatech.g73.app.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public class MyApplicationsController implements FXController<String, String> {

    @FXML
    private JFXButton backButton;

    @FXML
    void back(ActionEvent event) {
        try {
            App.getInstance().showScene("/userpage.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void initialize() {

    }

    @Override public void initData(Map<String, String> info) {
        return;
    }
}
