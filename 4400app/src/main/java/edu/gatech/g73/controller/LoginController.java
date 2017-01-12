package edu.gatech.g73.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.gatech.g73.app.App;
import edu.gatech.g73.app.user.UserManager;
import edu.gatech.g73.app.user.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/3/16
 */
public class LoginController implements FXController<String, String> {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize() {
        initializeFields();
    }

    @Override
    public void initData(Map<String, String> info) {
        if (info == null) return;
        if (info.containsKey("username")) {
            usernameField.setText(info.getOrDefault("username", ""));
        }
        if (info.containsKey("password")) {
            passwordField.setText(info.getOrDefault("password", ""));
        }
    }

    private boolean isInputValid() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            return UserManager.logInUser(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initializeFields() {
        usernameField.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            errorLabel.setText("");
        });
        passwordField.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            errorLabel.setText("");
        });
    }

    @FXML
    private void login() {
        try {
            if (isInputValid()) {
                if (UserManager.getLoggedInUser().getUserType() == UserType.ADMIN) {
                    App.getInstance().showScene("/adminpage.fxml", null);
                } else {
                    App.getInstance().showScene("/userpage.fxml", null);
                }
            }
            else {
                errorLabel.setText("Please check your Username or Password");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void register() {
        try {
            App.getInstance().showScene("/register.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
