package edu.gatech.g73.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/3/16
 */
public class EditProfileController implements FXController<String, String> {

    public static final String FETCH_MAJORS_QUERY = "SELECT Name FROM Major;";

    @FXML
    private Label departmentLabel;

    @FXML
    private JFXComboBox<String> yearBox;

    @FXML
    private JFXComboBox<String> majorBox;

    @FXML
    private JFXButton backButton;

    @Override
    public void initialize() {
        initializeYearBox();
        initializeMajorBox();
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

    private List<String> getAllMajors() throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  rs         = statement.executeQuery(FETCH_MAJORS_QUERY);
        List<String> majors = new ArrayList<>();
        while (rs.next()) {
            majors.add(rs.getString("Name"));
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
            years.add(year.getName());
        }
        return years;
    }

    private void initializeMajorBox() {
        try {
            majorBox.getItems().addAll(getAllMajors());
            User current = UserManager.getLoggedInUser();
            majorBox.setValue(current.getMajor());
            departmentLabel.setText(getDepartmentFromMajor(current.getMajor()));

            majorBox.setOnAction((event) -> {
                try {
                    String major = majorBox.getValue();
                    updateMajor(major);
                    String newMajor = UserManager.getLoggedInUser().getMajor();
                    departmentLabel.setText(getDepartmentFromMajor(newMajor));
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
            yearBox.getItems().addAll(getAllYears());
            yearBox.setValue(UserManager.getLoggedInUser().getYear().getName());

            yearBox.setOnAction((event) -> {
                try {
                    String year = yearBox.getValue();
                    updateYear(year);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMajor(String newMajor) throws Exception {
        HashMap<UserColumn, String> update = new HashMap<UserColumn, String>() {{
            put(UserColumn.MAJOR, newMajor);
        }};

        String updateQuery = UserManager.getLoggedInUser().getUpdateQuery(update);

        Connection connect = new SQLConnector().connect();
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(updateQuery);

        if (stmt != null) stmt.close();
        if (connect != null) connect.close();

        String username = UserManager.getLoggedInUser().getUsername();
        String password = UserManager.getLoggedInUser().getPassword();
        UserManager.logInUser(username, password);
    }

    private void updateYear(String newYear) throws Exception {
        HashMap<UserColumn, String> update = new HashMap<UserColumn, String>() {{
            put(UserColumn.YEAR, newYear);
        }};

        String updateQuery = UserManager.getLoggedInUser().getUpdateQuery(update);
        Connection connect = new SQLConnector().connect();
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(updateQuery);

        if (stmt != null) stmt.close();
        if (connect != null) connect.close();

        String username = UserManager.getLoggedInUser().getUsername();
        String password = UserManager.getLoggedInUser().getPassword();
        UserManager.logInUser(username, password);
    }

    @FXML
    private void back() {
        try {
            App.getInstance().showScene("/userpage.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
