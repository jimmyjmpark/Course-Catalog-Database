package edu.gatech.g73.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.gatech.g73.app.applyable.project.Project;
import edu.gatech.g73.app.user.User;
import edu.gatech.g73.app.user.UserManager;
import edu.gatech.g73.db.SQLConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public class ProjectWindowController implements FXController<String, String> {

    private Project proj;

    @FXML
    private Label titleLabel;

    @FXML
    private JFXButton applyButton;

    @FXML
    private Label estNumLabel;

    @FXML
    private Label requirementsLabel;

    @FXML
    private JFXTextArea description;

    @FXML
    private Label categoriesLabel;

    @FXML
    private Label designationLabel;

String total;
    @FXML
    private Label contactLabel;

    @FXML
    private Label advisorLabel;

    @FXML
    void apply(ActionEvent event) {
        try {
            User current = UserManager.getLoggedInUser();
            current.apply(titleLabel.getText());
            applyButton.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void initialize() {

    }

    @Override public void initData(Map<String, String> info) {
        if (info.containsKey("AdvisorName")) {
            advisorLabel.setText(info.get("AdvisorName"));
        }
        if (info.containsKey("EstNumStudents")) {
            estNumLabel.setText(info.get("EstNumStudents"));
        }
        if (info.containsKey("Categories")) {
            categoriesLabel.setText(info.get("Categories"));
        }
        if (info.containsKey("Description")) {
            description.setText(info.get("Description"));
        }
        if (info.containsKey("Designation")) {
            designationLabel.setText(info.get("Designation"));
        }
        if (info.containsKey("AdvisorEmail")) {
            contactLabel.setText(info.get("AdvisorEmail"));
        }
        if (info.containsKey("Requirements")) {
            requirementsLabel.setText(info.get("Requirements"));
        }
        if (info.containsKey("ProjectName")) {
            titleLabel.setText(info.get("ProjectName"));
        }

        try {
            System.out.println(titleLabel.getText());
            Connection sql = new SQLConnector().connect();
            Statement s = sql.createStatement();
            ResultSet rs = s.executeQuery(
                    String.format("SELECT * FROM Project WHERE Name='%s';", titleLabel.getText())
            );
            ResultSet rs2 = s.executeQuery(String.format("SELECT Major from User where Username ='UserManager.getLoggedInUser'"));

            while (rs.next()) {
                proj = new Project(rs, rs.getRow());
            }

            while (rs2.next()) {
              total = rs2.getString("Major");
            }

            if (!proj.canApply(UserManager.getLoggedInUser())) {
                applyButton.setDisable(true);
            }
            if (total == null) {
              applyButton.setDisable(true);
            }
        } catch (Exception e) {

        }
    }
}
