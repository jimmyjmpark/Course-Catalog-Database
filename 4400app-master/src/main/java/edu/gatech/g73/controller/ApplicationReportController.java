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



public class ApplicationReportController implements FXController<String, String> {
    private ObservableList<ObservableList> data;
    public static final String FETCH_QUERY = "select A.Project_name as 'Project', ApplicantNumber, AR*100.0/TR as 'accept rate', Top3Majors as 'Top 3 Major' from    	(select Project_name, count(Student_name) as ApplicantNumber from Apply group by Project_name order by count(Student_name) desc) as A,    	(SELECT everything.Project_name, COUNT(accept.Project_name) as AR FROM (select * from Apply) as everything LEFT OUTER JOIN (select * from Apply where Status = 'A') as accept ON everything.Student_name = accept.Student_name and everything.Project_name = accept.Project_name GROUP BY everything.Project_name) as B,    	(select Project_name, count(*) as TR from Apply group by Project_name) as C,    	(select Project_name, GROUP_CONCAT(Major SEPARATOR '/') as Top3Majors    	from (select Project_name, Major from (select Major, Project_name, topMajor, @rowVal := if(@prev = Project_name, @rowVal + 1, 1) AS rowVal, @prev := Project_name    	from (select Project_name, Major, count(Major) as topMajor from Apply, User where Student_name = Username group by Project_name, Major) as t    	join (select @prev := NULL, @rowVal := 0) as t1    	order by topMajor DESC) as t2 where rowVal <= 3) as t3 group by Project_name) as D    where A.Project_name = B.Project_name and A.Project_name = C.Project_name and A.Project_name = D.Project_name;";
    public static final String FETCH_TOTAL = "select count(*) as c from Apply";
    public static final String FETCH_ACCEPT = "select count(*) as c1 from Apply where Status = 'A'";

    @FXML
    private TableView table;

    @FXML
    private Label AppLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private JFXButton backButton;

    String total;
    String accepted;

    @Override
    public void initialize() {
      buildData();
      initLabel();
    }

    @Override
    public void initData(Map<String, String> info) {
    }

    public void initLabel() {
            try{
      Connection connection = new SQLConnector().connect();
      Statement  statement  = connection.createStatement();
      ResultSet  rs1    = statement.executeQuery(FETCH_TOTAL);
      while (rs1.next()) {
        total = rs1.getString("c");
      }
      ResultSet  rs2    = statement.executeQuery(FETCH_ACCEPT);
      while (rs2.next()) {
        accepted = rs2.getString("c1");
      }
      infoLabel.setText(total + " applications in total, accepted " + accepted + " applications");
    } catch (IOException e) {
        e.printStackTrace();
    } catch (java.sql.SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
