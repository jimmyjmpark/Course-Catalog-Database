package edu.gatech.g73.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.gatech.g73.app.App;
import edu.gatech.g73.app.applyable.Searchable;
import edu.gatech.g73.app.applyable.course.Course;
import edu.gatech.g73.app.applyable.project.Project;
import edu.gatech.g73.app.user.UserYear;
import edu.gatech.g73.db.SQLConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public class UserPageController implements FXController<String, String> {

    public static final String GET_DESIGNATIONS = "SELECT Name FROM Designation;";
    public static final String GET_CATEGORIES = "SELECT Name FROM Category;";
    public static final String GET_MAJORS = "SELECT Name FROM Major;";

    @FXML
    private TableView<Searchable> resultsList;

    @FXML
    private TableColumn<Searchable, String> nameColumn;

    @FXML
    private TableColumn<Searchable, String> typeColumn;

    @FXML
    private JFXButton filterButton;

    @FXML
    private JFXButton resetButton;

    @FXML
    private JFXTextField titleField;

    @FXML
    private JFXComboBox<String> designationBox;

    @FXML
    private JFXComboBox<String> majorBox;

    @FXML
    private JFXComboBox<String> yearBox;

    @FXML
    private JFXComboBox<String> filterBox;

    @FXML
    private JFXComboBox<String> categoryBox;

    @FXML
    private JFXButton categoryButton;

    @FXML
    private ListView<String> categoryList;

    @FXML
    private JFXButton editProfileButton;

    @FXML
    private JFXButton viewApplicationsButton;

    @FXML
    private AnchorPane detailsPane;

    private ObservableList<String> categories;
    private ObservableList<String> designations;
    private ObservableList<String> majors;
    private ObservableList<String> years;
    private ObservableList<String> filters;


    @Override public void initialize() {
        try {
            initializeDesignationBox();
            initializeMajorBox();
            initializeYearBox();
            initializeFilterBox();
            initializeCategoryBox();
            initializeSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeSearch() throws Exception {
        nameColumn.setCellValueFactory((cellData) -> cellData.getValue().getNameValue());
        typeColumn.setCellValueFactory((cellData) -> cellData.getValue().getTypeValue());
        search("Project", "");
        search("Course", "");
        // http://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx
        resultsList.setRowFactory((e) -> {
            TableRow<Searchable> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                if (event.getClickCount() > 1) {
                    processResultSelect(row.getItem());
                }
            });
            return row;
        });
    }

    private void search(String tablename, String condition) throws Exception {
        Connection sql = new SQLConnector().connect();
        Statement statement = sql.createStatement();
        ResultSet rs = statement.executeQuery(
                String.format("SELECT * FROM %s %s;", tablename, condition)
        );
        List<Searchable> tmp = new ArrayList<>();

        while (rs.next()) {
            Searchable candidate;
            if (tablename.equals("Project")) {
                candidate = new Project(rs, rs.getRow());
            } else {
                candidate = new Course(rs, rs.getRow());
            }
            tmp.add(candidate);
        }
        resultsList.getItems().addAll(tmp);
    }

    @Override public void initData(Map<String, String> info) {
        return;
    }

    private void initializeDesignationBox() throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  results    = statement.executeQuery(GET_DESIGNATIONS);

        List<String> tmp = new ArrayList<>();
        while (results.next()) {
            tmp.add(results.getString("Name"));
        }
        designations = FXCollections.observableList(tmp);

        designationBox.setItems(designations);

        if (results != null) results.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    private void initializeMajorBox() throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  results    = statement.executeQuery(GET_MAJORS);

        List<String> tmp = new ArrayList<>();
        while (results.next()) {
            tmp.add(results.getString("Name"));
        }
        majors = FXCollections.observableList(tmp);

        majorBox.setItems(majors);

        if (results != null) results.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    private void initializeYearBox() {
        List<String> tmp = new ArrayList<>();
        for (UserYear year : UserYear.values()) {
            tmp.add(year.getName());
        }
        years = FXCollections.observableList(tmp);

        yearBox.setItems(years);
    }

    private void initializeFilterBox() {
        List<String> tmp = new ArrayList<>();
        tmp.add("Courses");
        tmp.add("Projects");
        tmp.add("Both");
        filters = FXCollections.observableList(tmp);
        filterBox.setItems(filters);
        filterBox.setValue(filters.get(filters.size() - 1));
    }

    @FXML
    private void search(ActionEvent event) {
        try {
            String     filter    = filterBox.getValue();
            String     queryCourses     = "SELECT * FROM Course;";   // TODO
            String     queryProjects     = "SELECT * FROM Project;"; // TODO
            List<Searchable> results = new ArrayList<>();
            if (filter.equals("Both") || filter.equals("Courses")) {
                results.addAll(searchCourses(queryCourses));
            }
            if (filter.equals("Both") || filter.equals("Projects")) {
                results.addAll(searchProjects(queryProjects));
            }
            resultsList.setItems(FXCollections.observableList(results));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Searchable> searchCourses(String query) throws Exception {
        Connection sql       = new SQLConnector().connect();
        Statement  statement = sql.createStatement();
        ResultSet  results   = statement.executeQuery(query);

        List<Searchable> stuff = new ArrayList<>();
        while (results.next()) {
            stuff.add(new Course(results, results.getRow()));
        }

        if (results != null) results.close();
        if (statement != null) statement.close();
        if (sql != null) sql.close();

        return stuff;
    }

    private List<Searchable> searchProjects(String query) throws Exception {
        Connection sql       = new SQLConnector().connect();
        Statement  statement = sql.createStatement();
        ResultSet  results   = statement.executeQuery(query);

        List<Searchable> stuff = new ArrayList<>();
        while (results.next()) {
            stuff.add(new Project(results, results.getRow()));
        }

        if (results != null) results.close();
        if (statement != null) statement.close();
        if (sql != null) sql.close();

        return stuff;
    }

    @FXML
    private void reset(ActionEvent event) {
        titleField.setText("");
        designationBox.setValue(null);
        majorBox.setValue(null);
        yearBox.setValue(null);
        filterBox.setValue(filters.get(filters.size() - 1));
        categoryBox.setValue(null);

        try {
            initializeCategoryBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
        categoryList.getItems().clear();

        if (resultsList != null) resultsList.getItems().clear();
        try {
            initializeSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCategoryBox() throws Exception {
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  results    = statement.executeQuery(GET_CATEGORIES);

        List<String> tmp = new ArrayList<>();
        while (results.next()) {
            tmp.add(results.getString("Name"));
        }
        categories = FXCollections.observableList(tmp);

        categoryBox.setItems(categories);

        if (results != null) results.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    @FXML
    private void addCategory(ActionEvent event) {
        String cat = categoryBox.getValue();
        categoryList.getItems().add(cat);
        categories.remove(cat);
    }

    @FXML
    private void renderEditProfile(ActionEvent event) {
        try {
            App.getInstance().showScene("/editprofile.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void renderMyApplications(ActionEvent event) {
        try {
            App.getInstance().showScene("/myapplications.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processResultSelect(Searchable item) {
        if (item == null) return;
        try {
            if (item instanceof Course) {
                renderCourseWindow((Course) item);
            } else if (item instanceof Project) {
                renderProjectWindow((Project) item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderCourseWindow(Course c) throws Exception {
        Stage popup = new Stage();
        popup.setTitle("Course");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/coursewindow.fxml"));
        Parent     page   = loader.load();
        FXController controller = loader.getController();
        if (c.exportValues() != null) {
            controller.initData(c.exportValues());
        }
        Scene scene = popup.getScene();
        if (scene == null) {
            scene = new Scene(page, 640, 480);
            // scene.getStylesheets().add(App.class.getResource("default.css").toExternalForm());
            popup.setScene(scene);
        } else {
            popup.getScene().setRoot(page);
        }
        popup.sizeToScene();
        popup.show();
    }

    private void renderProjectWindow(Project p) throws Exception {
        Stage popup = new Stage();
        popup.setTitle("Course");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/projectwindow.fxml"));
        Parent     page   = loader.load();
        FXController controller = loader.getController();
        if (p.exportValues() != null) {
            controller.initData(p.exportValues());
        }
        Scene scene = popup.getScene();
        if (scene == null) {
            scene = new Scene(page, 640, 480);
            // scene.getStylesheets().add(App.class.getResource("default.css").toExternalForm());
            popup.setScene(scene);
        } else {
            popup.getScene().setRoot(page);
        }
        popup.sizeToScene();
        popup.show();
    }

}
