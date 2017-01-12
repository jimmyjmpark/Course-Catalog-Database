package edu.gatech.g73.controller;

import javafx.scene.control.Hyperlink;
import edu.gatech.g73.app.App;
import edu.gatech.g73.app.user.User;
import edu.gatech.g73.app.user.UserColumn;
import edu.gatech.g73.app.user.UserManager;
import edu.gatech.g73.app.user.UserYear;
import edu.gatech.g73.db.SQLConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class AdminPageController implements FXController<String, String> {
    @FXML
    private Label departmentLabel;

    @FXML
    private Hyperlink viewAppHyperlink;
    @FXML
    private Hyperlink viewPopHyperlink;
    @FXML
    private Hyperlink viewAppRepHyperlink;

    @FXML
    private Hyperlink addProjHyperlink;
    @FXML
    private Hyperlink addCourseHyperlink;

    @Override
    public void initialize() {
    }

    @Override
    public void initData(Map<String, String> info) {
        return;
    }

    @FXML
    private void viewApplications() {
        try {
            App.getInstance().showScene("/viewapplications.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewPopularProject() {
        try {
            App.getInstance().showScene("/popularprojects.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewAppReport() {
        try {
            App.getInstance().showScene("/applicationreport.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addProject() {
        try {
            App.getInstance().showScene("/addproject.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addCourse() {
        try {
            App.getInstance().showScene("/addcourse.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
