package edu.gatech.g73.app.applyable.project;

import edu.gatech.g73.app.applyable.Applyable;
import edu.gatech.g73.app.user.User;
import edu.gatech.g73.app.user.UserManager;
import edu.gatech.g73.db.SQLConnector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public class Project implements Applyable {

    public static final String SELECT_ALL = "SELECT * FROM Project;";
    public static final String UPDATE_STATUS = "UPDATE Apply SET %s='%s' WHERE %s='%s' AND "
                                               + "%s='%s';";

    private String name;
    private String description;
    private String advisorEmail;
    private String advisorName;
    private String estStudents;
    private String designation;

    public Project(ResultSet rs, int row) {
        try {
            rs.absolute(row);
            name = rs.getString(ProjectColumn.NAME.getName());
            description = rs.getString(ProjectColumn.DESCRIPTION.getName());
            advisorName = rs.getString(ProjectColumn.ADVISOR_NAME.getName());
            advisorEmail = rs.getString(ProjectColumn.ADVISOR_EMAIL.getName());
            estStudents = rs.getString(ProjectColumn.EST_STUDENTS.getName());
            designation = rs.getString(ProjectColumn.DESIGNATION_NAME.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Project(String name,
                   String description,
                   String advisorEmail,
                   String advisorName,
                   String estStudents,
                   String designation) {
        this.name = name;
        this.description = description;
        this.advisorEmail = advisorEmail;
        this.advisorName = advisorName;
        this.estStudents = estStudents;
        this.designation = designation;
    }


    @Override
    public boolean canApply(User user) {
        try {
            Connection conn = new SQLConnector().connect();
            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery(
                    String.format("SELECT * FROM Apply WHERE Student_name='%s';", user.getUsername())
            );

            boolean permission = true;
            while (r.next()) {
                if (r.getString("Project_name").equals(name)
                        && r.getString("Student_name").equals(user.getUsername())) {
                    permission = false;
                    break;
                }
            }

            if (r != null) r.close();
            if (s != null) s.close();
            if (conn != null) conn.close();

            return permission;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void apply() throws Exception {
        User usr = UserManager.getLoggedInUser();
        apply(usr);
    }

    @Override
    public void apply(User user) throws Exception {
        // http://stackoverflow.com/questions/1081234/java-date-insert-into-database
        if (canApply(user)) {
            Connection sql       = new SQLConnector().connect();
            PreparedStatement stmt = getApplyStatement(user, sql);
            stmt.executeUpdate();

            if (stmt != null) stmt.close();
            if (sql != null) sql.close();
        } else {
            return;
        }
    }

    @Override public String selectAllQuery() {
        return SELECT_ALL;
    }

    private PreparedStatement getApplyStatement(User user, Connection sql) throws Exception {
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        PreparedStatement statement = sql.prepareStatement(
                "INSERT INTO Apply VALUES (?, ?, ?, ?);");
        statement.setString(1, name);
        statement.setString(2, user.getUsername());
        statement.setDate(3, now);
        statement.setString(4, "P");

        return statement;
    }

    @Override public String getType() {
        return "Project";
    }

    @Override public String getName() {
        return name;
    }

    @Override public StringProperty getNameValue() {
        return new SimpleStringProperty(name);
    }

    @Override public StringProperty getTypeValue() {
        return new SimpleStringProperty(getType());
    }

    public List<String> getCategories() throws Exception {
        ArrayList<String> reqList = new ArrayList<>();
        Connection connector = new SQLConnector().connect();
        Statement  statement = connector.createStatement();
        ResultSet rs = statement.executeQuery(
                String.format("SELECT Project_name FROM Project_is_category WHERE "
                              + "%s='%s';",
                              "Project_name", name)
        );
        while (rs.next()) {
            reqList.add(rs.getString("Project_name"));
        }

        if (rs != null) rs.close();
        if (statement != null) statement.close();
        if (connector != null) connector.close();

        return reqList;
    }

    public List<String> getRequirements() throws Exception {
        ArrayList<String> reqList = new ArrayList<>();
        Connection connector = new SQLConnector().connect();
        Statement  statement = connector.createStatement();
        ResultSet rs = statement.executeQuery(
                String.format("SELECT Requirement FROM Project_requirement WHERE "
                              + "%s='%s';",
                              "Name", name)
        );
        while (rs.next()) {
            reqList.add(rs.getString("Requirement"));
        }

        if (rs != null) rs.close();
        if (statement != null) statement.close();
        if (connector != null) connector.close();

        return reqList;
    }

    @Override
    public Map<String, String> exportValues() {
        String cats    = "";
        String reqs = "";
        try {
            List<String> catList = getCategories();
            List<String> reqList = getRequirements();

            Iterator<String> ci = catList.iterator();
            while (ci.hasNext()) {
                cats += ci.next();
                if (ci.hasNext()) {
                    cats += ", ";
                }
            }

            Iterator<String> ri = reqList.iterator();
            while (ri.hasNext()) {
                reqs += ri.next();
                if (ri.hasNext()) {
                    reqs += ", ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String categories = cats;
        String requirements = reqs;
        return new HashMap<String, String>() {{
            put("AdvisorName", advisorName);
            put("EstNumStudents", estStudents);
            put("Categories", categories);
            put("Description", description);
            put("Designation", designation);
            put("AdvisorEmail", advisorEmail);
            put("Requirements", requirements);
            put("ProjectName", name);
        }};
    }

    public String getDescription() {
        return description;
    }

    public String getAdvisorEmail() {
        return advisorEmail;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public String getEstStudents() {
        return estStudents;
    }

    public String getDesignation() {
        return designation;
    }
}
