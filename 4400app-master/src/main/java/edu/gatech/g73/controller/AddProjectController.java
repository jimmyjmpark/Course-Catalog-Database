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

public class AddProjectController implements FXController<String, String> {
  // - insert into Project values (NAME_INPUT, DESCRIPTION_INPUT, ADVIS_EMAIL_INPUT, ADVIS_INPUT, ESTNUM_INPUT, DESIGNATION_INPUT);
  // - insert into Project_is_category values (NAME_INPUT, CATEGORY_INPUT);
  // for each requirement
  // - insert into Project_requirement values (NAME_INPUT, REQUIREMENT);

    private String NAME_INPUT;
    private String DESCRIPTION_INPUT;
    private String ADVIS_EMAIL_INPUT;
    private String ADVIS_INPUT;
    private String ESTNUM_INPUT;
    private String DESIGNATION_INPUT;
    private ObservableList<String> categories;
    private String MAJ_REQ;
    private String YEAR_REQ;
    private String DEPART_REQ;

    public static final String FETCH_MAJORS_QUERY = "SELECT Name FROM Major;";
    public static final String FETCH_DEPART_QUERY = "SELECT Name FROM Department;";
    public static final String FETCH_DESIG_QUERY = "SELECT Name FROM Designation;";
    public static final String FETCH_CATE_QUERY = "SELECT Name FROM Category;";

    @FXML
    private Label errorLabel;

    @FXML
    private JFXTextField pNameInput;
    @FXML
    private JFXTextField advInput;
    @FXML
    private JFXTextField advEInput;
    @FXML
    private JFXTextField descInput;
    @FXML
    private JFXTextField estStuInput;

    @FXML
    private JFXComboBox<String> yearInput;

    @FXML
    private JFXComboBox<String> majInput;

    @FXML
    private JFXComboBox<String> departInput;

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
      initializeYearBox();
      initializeMajorBox();
      initializeDepartBox();
      initializeDesigBox();
      initializeCategoryBox();
    }

    @Override
    public void initData(Map<String, String> info) {
        return;
    }

    private String getDepartmentFromMajor(String major) throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
                String.format("SELECT * FROM Major WHERE Name='%s';", major)
        );

        String department = "Not Found";

        while (rs.next()) {
            String candidateMajor = rs.getString("Name");

            if (candidateMajor.equals(major)) {
                department = rs.getString("Dept_name");
            }
        }

        return department;
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

    private List<String> getAllDepart() throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  rs         = statement.executeQuery(FETCH_DEPART_QUERY);
        List<String> departs = new ArrayList<>();
        while (rs.next()) {
            departs.add(rs.getString("Name"));
        }

        if (rs != null) rs.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();

        Collections.sort(departs);
        return departs;
    }

    private void initializeDepartBox() {
        try {
            departInput.getItems().addAll(getAllDepart());

            departInput.setOnAction((event) -> {
                try {
                    DEPART_REQ = departInput.getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getAllMajors() throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  rs         = statement.executeQuery(FETCH_MAJORS_QUERY);
        List<String> majors = new ArrayList<>();
        while (rs.next()) {
            majors.add(rs.getString("Name") + " Students Only");
        }

        if (rs != null) rs.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();

        Collections.sort(majors);
        return majors;
    }

    private List<String> getAllYears() {
        List<String> years = new ArrayList<>();
        for (UserYear year : UserYear.values()) {
            years.add(year.getName() + "s Only");
          }
        return years;
    }

    private void initializeMajorBox() {
        try {
            majInput.getItems().addAll(getAllMajors());

            majInput.setOnAction((event) -> {
                try {
                    MAJ_REQ = majInput.getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeYearBox() {
        try {
            yearInput.getItems().addAll(getAllYears());

            yearInput.setOnAction((event) -> {
                try {
                    YEAR_REQ = yearInput.getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void submit() {
      String addProjReqDepart;
      String addProjReqMaj;
      String addProjReqYear;

      NAME_INPUT = pNameInput.getText();
      ADVIS_INPUT = advInput.getText();
      ADVIS_EMAIL_INPUT = advEInput.getText();
      DESCRIPTION_INPUT = descInput.getText();
      ESTNUM_INPUT = estStuInput.getText();

      if(NAME_INPUT == null || ADVIS_INPUT == null || ADVIS_EMAIL_INPUT == null || DESCRIPTION_INPUT == null || ESTNUM_INPUT == null || categoryList.getItems() == null || DESIGNATION_INPUT == null) {
        errorLabel.setText("Please fill in the required fields");
        return;
      }
      try {
          String addProjectString = String.format("INSERT INTO Project VALUES ('%s', '%s', '%s', '%s', '%s', '%s');", NAME_INPUT, DESCRIPTION_INPUT, ADVIS_EMAIL_INPUT, ADVIS_INPUT, ESTNUM_INPUT, DESIGNATION_INPUT);

          Connection connect = new SQLConnector().connect();
          Statement stmt = connect.createStatement();
          stmt.executeUpdate(addProjectString);

          for(String c : categoryList.getItems()) {
            String addProjCate = String.format("INSERT INTO Project_is_category VALUES ('%s', '%s');", NAME_INPUT, c);
            stmt.executeUpdate(addProjCate);
          }

          if(DEPART_REQ != null) {
            addProjReqDepart = String.format("INSERT INTO Project_requirement VALUES ('%s', '%s');", NAME_INPUT, DEPART_REQ);
            stmt.executeUpdate(addProjReqDepart);
          }
          if(MAJ_REQ != null) {
            MAJ_REQ = MAJ_REQ.substring(0,MAJ_REQ.length()-14);
            addProjReqMaj = String.format("INSERT INTO Project_requirement VALUES ('%s', '%s');", NAME_INPUT, MAJ_REQ);
            stmt.executeUpdate(addProjReqMaj);
          }
          if(YEAR_REQ != null) {
            switch(YEAR_REQ.charAt(1)) {
              case 'r' : YEAR_REQ = "0"; break;
              case 'o' : YEAR_REQ = "1"; break;
              case 'u' : YEAR_REQ = "2"; break;
              case 'e' : YEAR_REQ = "3"; break;
            }

            addProjReqYear = String.format("INSERT INTO Project_requirement VALUES ('%s', '%s');", NAME_INPUT, YEAR_REQ);
            stmt.executeUpdate(addProjReqYear);
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
