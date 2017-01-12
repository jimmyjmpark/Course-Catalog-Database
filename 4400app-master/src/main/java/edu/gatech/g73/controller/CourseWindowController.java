package edu.gatech.g73.controller;

import edu.gatech.g73.app.applyable.Searchable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public class CourseWindowController implements FXController<String, String>{

    @FXML
    private Label titleLabel;

    @FXML
    private Label estNumLabel;

    @FXML
    private Label categoriesLabel;

    @FXML
    private Label designationLabel;

    @FXML
    private Label instructorLabel;

    @FXML
    private Label courseLabel;

    @Override public void initialize() {

    }

    @Override public void initData(Map<String, String> info) {
        if (info.containsKey("CourseNumber")) {
            titleLabel.setText(info.get("CourseNumber"));
        }
        if (info.containsKey("EstNumStudents")) {
            estNumLabel.setText(info.get("EstNumStudents"));
        }
        if (info.containsKey("Categories")) {
            categoriesLabel.setText(info.get("Categories"));
        }
        if (info.containsKey("Designation")) {
            designationLabel.setText(info.get("Designation"));
        }
        if (info.containsKey("Instructor")) {
            instructorLabel.setText(info.get("Instructor"));
        }
        if (info.containsKey("CourseName")) {
            courseLabel.setText(info.get("CourseName"));
        }
    }
}
