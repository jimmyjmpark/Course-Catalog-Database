package edu.gatech.g73.app.applyable.course;

import edu.gatech.g73.app.applyable.Searchable;
import edu.gatech.g73.db.SQLConnector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public class Course implements Searchable {

    public static final String SELECT_ALL = "SELECT * FROM Course;";

    private String name;
    private String courseNumber;
    private String instructor;
    private String estStudents;
    private String designation;

    public Course(ResultSet rs, int row) {
        try {
            rs.absolute(row);
            name = rs.getString(CourseColumn.NAME.getName());
            courseNumber = rs.getString(CourseColumn.COURSE_NUMBER.getName());
            instructor = rs.getString(CourseColumn.INSTRUCTOR.getName());
            estStudents = rs.getString(CourseColumn.EST_STUDENTS.getName());
            designation = rs.getString(CourseColumn.DESIGNATION_NAME.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Course(String name,
                   String courseNumber,
                   String instructor,
                   String estStudents,
                   String designation) {
        this.name = name;
        this.courseNumber = courseNumber;
        this.instructor = instructor;
        this.estStudents = estStudents;
        this.designation = designation;
    }


    @Override public String selectAllQuery() {
        return SELECT_ALL;
    }

    @Override public String getType() {
        return "Course";
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
        ArrayList<String> catList = new ArrayList<>();
        Connection connector = new SQLConnector().connect();
        Statement  statement = connector.createStatement();
        ResultSet rs = statement.executeQuery(
                String.format("SELECT Course_name FROM Course_is_category WHERE "
                              + "%s='%s';",
                              "Course_name", name)
        );
        while (rs.next()) {
            catList.add(rs.getString("Course_name"));
        }

        if (rs != null) rs.close();
        if (statement != null) statement.close();
        if (connector != null) connector.close();

        return catList;
    }

    @Override public Map<String, String> exportValues() {
        String cats = "";
        try {
            List<String> catList = getCategories();
            Iterator<String> catIterator = catList.iterator();
            while (catIterator.hasNext()) {
                cats += catIterator.next();
                if (catIterator.hasNext()) {
                    cats += ", ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String categories = cats;
        return new HashMap<String, String>() {{
            put("CourseNumber", courseNumber);
            put("EstNumStudents", estStudents);
            put("Categories", categories);
            put("Designation", designation);
            put("Instructor", instructor);
            put("CourseName", name);
        }};
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getEstStudents() {
        return estStudents;
    }

    public String getDesignation() {
        return designation;
    }
}
