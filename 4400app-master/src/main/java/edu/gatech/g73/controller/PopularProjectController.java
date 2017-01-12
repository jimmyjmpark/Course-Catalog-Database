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



public class PopularProjectController implements FXController<String, String> {
    private ObservableList<ObservableList> data;
    public static final String FETCH_QUERY = "select Project_name as 'Project', count(Student_name) as '# of Applicants' from Apply group by Project_name order by count(Student_name) desc limit 10;";

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
            App.getInstance().showScene("/adminpage.fxml", null);
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

}
