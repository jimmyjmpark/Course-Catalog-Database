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
import javafx.util.Callback;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;



public class ViewApplicationsController implements FXController<String, String> {
    private ObservableList<ObservableList> data;
    public static final String FETCH_QUERY = "select Apply.Project_name as 'Project', User.Username as 'Username', User.Major as 'Applicant Major', User.Year as 'Applicant Year', Apply.Status as 'Status' from Project natural join User, Apply where Apply.Project_name = Project.Name and Apply.Student_name = User.username;";
    private ObservableList<ObservableList> projUserList;

    @FXML
    private TableView table;

    @FXML
    private Label AppLabel;

    @FXML
    private JFXButton backButton;

    @Override
    public void initialize() {
      buildData();
    }

    @Override
    public void initData(Map<String, String> info) {
    }

    public void buildData() {
      data = FXCollections.observableArrayList();

      try{
        Connection connection = new SQLConnector().connect();
        Statement  statement  = connection.createStatement();
        ResultSet  rs    = statement.executeQuery(FETCH_QUERY);

        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){

        final int j = i;
        TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
        col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(j).toString());
            }
        });

        table.getColumns().addAll(col);
      }

    while(rs.next()){
        //Iterate Row
        ObservableList<String> row = FXCollections.observableArrayList();
        for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
            //Iterate Column
            row.add(rs.getString(i));
        }
        data.add(row);
        table.getItems().add(row);
    }

    //FINALLY ADDED TO TableView
    table.setItems(data);
    }catch(Exception e){
      e.printStackTrace();
    }
    }

    @FXML
    private void back() {
        try {
          ObservableList selectedItems = table.getSelectionModel().getSelectedItems();
          PROJ_INPUT = selectedItems.get(0);
          NAME_INPUT = selectedItems.get(0);
          String rejectProject = String.format("update Apply set status = 'a' where Apply.Project_name = '%s' and Apply.Student_name = '%s';", PROJ_INPUT, NAME_INPUT);
            App.getInstance().showScene("/adminpage.fxml", null);
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    @FXML
    private void reject() {
        try {
          ObservableList selectedItems = table.getSelectionModel().getSelectedItems();
          PROJ_INPUT = selectedItems.get(0);
          NAME_INPUT = selectedItems.get(0);
          String rejectProject = String.format("update Apply set status = 'a' where Apply.Project_name = '%s' and Apply.Student_name = '%s';", PROJ_INPUT, NAME_INPUT);
            App.getInstance().showScene("/adminpage.fxml", null);
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    @FXML
    private void accept() {
        try {
            App.getInstance().showScene("/adminpage.fxml", null);
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

}
