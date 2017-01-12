package edu.gatech.g73.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.gatech.g73.app.App;
import edu.gatech.g73.app.user.User;
import edu.gatech.g73.app.user.UserManager;
import edu.gatech.g73.db.SQLConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/3/16
 */
public class RegisterController implements FXController<String, String> {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXPasswordField passwordField1;

    @FXML
    private JFXPasswordField passwordField2;

    @FXML
    private Label errorLabel;

    private Map<String, String> forLogin;

    @Override
    public void initialize() {
        initializeFields();
    }

    @Override
    public void initData(Map<String, String> info) {
        return;
    }

    private void initializeFields() {
        usernameField.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            errorLabel.setText("");
        });
        emailField.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            errorLabel.setText("");
        });
        passwordField1.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            errorLabel.setText("");
        });
        passwordField2.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            errorLabel.setText("");
        });
    }

    private boolean isInputValid() throws Exception {
        User candidate = UserManager.getUserWithUsername(usernameField.getText());
        if (candidate != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!passwordField1.getText().equals(passwordField2.getText())) {
            throw new IllegalArgumentException("Passwords don't match");
        }
        if (!emailField.getText().contains("@gatech.edu")) {
            throw new IllegalArgumentException("E-Mail must be a valid @gatech.edu address");
        }
        return true;
    }

    @FXML
    private void register() {
        try {
            if (isInputValid()) {
                User newby = new User(usernameField.getText(),
                                      passwordField1.getText(),
                                      emailField.getText());
                Connection connection = new SQLConnector().connect();
                Statement statement = connection.createStatement();
                statement.executeUpdate(newby.getInsertQuery());

                forLogin = new HashMap<String, String>() {{
                    put("username", usernameField.getText());
                    put("password", passwordField1.getText());
                }};
                App.getInstance().showScene("/login.fxml", forLogin);
            }
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("An Error has Occurred");
            e.printStackTrace();
        }
    }

    @FXML
    private void back() {
        try {
            App.getInstance().showScene("/login.fxml", forLogin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
