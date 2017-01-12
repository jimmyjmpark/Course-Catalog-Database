package edu.gatech.g73.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.gatech.g73.app.App;
import edu.gatech.g73.app.user.User;
import edu.gatech.g73.app.user.UserColumn;
import edu.gatech.g73.app.user.UserManager;
import edu.gatech.g73.app.user.UserYear;
import edu.gatech.g73.db.SQLConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class AddCourseController implements FXController<String, String> {
  // - insert into Project values (NAME_INPUT, DESCRIPTION_INPUT, ADVIS_EMAIL_INPUT, ADVIS_INPUT, ESTNUM_INPUT, DESIGNATION_INPUT);
  // - insert into Project_is_category values (NAME_INPUT, CATEGORY_INPUT);
  // for each requirement
  // - insert into Project_requirement values (NAME_INPUT, REQUIREMENT);

    private String CNUM_INPUT;
    private String CNAME_INPUT;
    private String INSTR_INPUT;
    private String ESTNUM_INPUT;
    private String DESIGNATION_INPUT;
    private ObservableList<String> categories;

    public static final String FETCH_DESIG_QUERY = "SELECT Name FROM Designation;";
    public static final String FETCH_CATE_QUERY = "SELECT Name FROM Category;";

    @FXML
    private JFXTextField cNumInput;
    @FXML
    private JFXTextField cNameInput;
    @FXML
    private JFXTextField instrInput;
    @FXML
    private JFXTextField estStuInput;

    @FXML
    private Label errorLabel;

    @FXML
    private JFXComboBox<String> categoryBox;

    @FXML
    private JFXButton categoryButton;

    @FXML
    private ListView<String> categoryList;

    @FXML
    private JFXComboBox<String> desigInput;

    @Override
    public void initialize() {
      initializeDesigBox();
      initializeCategoryBox();
    }

    @Override
    public void initData(Map<String, String> info) {
        return;
    }

    private List<String> getAllDesig() throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  rs         = statement.executeQuery(FETCH_DESIG_QUERY);
        List<String> desigs = new ArrayList<>();
        while (rs.next()) {
            desigs.add(rs.getString("Name"));
        }

        if (rs != null) rs.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();

        Collections.sort(desigs);
        return desigs;
    }

    private void initializeDesigBox() {
        try {
            desigInput.getItems().addAll(getAllDesig());

            desigInput.setOnAction((event) -> {
                try {
                    DESIGNATION_INPUT = desigInput.getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCategoryBox() {
      try {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  results    = statement.executeQuery(FETCH_CATE_QUERY);

        List<String> tmp = new ArrayList<>();
        while (results.next()) {
            tmp.add(results.getString("Name"));
        }
        categories = FXCollections.observableList(tmp);

        categoryBox.setItems(categories);
        System.out.println("CATEGORY: " + categoryBox.getValue());

        if (results != null) results.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @FXML
    private void submit() {
      CNUM_INPUT = cNumInput.getText();
      CNAME_INPUT = cNameInput.getText();
      INSTR_INPUT = instrInput.getText();
      ESTNUM_INPUT = estStuInput.getText();

      if(CNUM_INPUT == null || CNAME_INPUT == null || INSTR_INPUT == null || ESTNUM_INPUT == null || categoryList.getItems() == null || DESIGNATION_INPUT == null) {
        errorLabel.setText("Please fill in the required fields");
        return;
      }

      try {
          String addCourseString = String.format("INSERT INTO Course VALUES ('%s', '%s', '%s', '%s', '%s');", CNAME_INPUT, CNUM_INPUT, INSTR_INPUT, ESTNUM_INPUT, DESIGNATION_INPUT);

          Connection connect = new SQLConnector().connect();
          Statement stmt = connect.createStatement();
          stmt.executeUpdate(addCourseString);

          for(String c : categoryList.getItems()) {
            String addCourseCate = String.format("INSERT INTO Course_is_category VALUES ('%s', '%s');", CNAME_INPUT, c);
            stmt.executeUpdate(addCourseCate);
          }

          App.getInstance().showScene("/adminpage.fxml", null);
      } catch (IOException e) {
          e.printStackTrace();
      } catch (java.sql.SQLException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @FXML
    private void addCategory(ActionEvent event) {
        String cat = categoryBox.getValue();
        categoryList.getItems().add(cat);
        categories.remove(cat);
    }

    @FXML
    private void back() {
        try {
            App.getInstance().showScene("/adminpage.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
