package edu.gatech.g73.app.user;

import edu.gatech.g73.app.applyable.project.Project;
import edu.gatech.g73.db.SQLConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Responsible for storing user's information while also generating any user-specific MySQL Queries
 *
 * Created by someone on 12/1/16.
 */
public class User {
    
    public static final UserYear DEFAULT_YEAR = UserYear.FRESHMAN;
    public static final String DEFAULT_MAJOR = "Architecture";
    public static final UserType DEFAULT_TYPE = UserType.STUDENT;
    
    private String  username;
    private String  password;
    private String  email;
    private UserYear  year;
    private String  major;
    private UserType userType;

    /**
     * Generates a User from a given ResultSet at a particular row
     * @param rs ResultSet to read from
     * @param row Row of ResultSet to read
     */
    public User(ResultSet rs, int row) {
        try {
            rs.absolute(row);
            this.username = rs.getString("Username");
            this.password = rs.getString("Password");
            this.email = rs.getString("Email");
            this.year = yearFromInt(rs.getInt("Year"));
            this.major = rs.getString("Major");
            this.userType = typeFromChar(rs.getString("UserType").charAt(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a user from String values, using default values for Year, Major, and Type.
     *
     * @param username
     * @param password
     * @param email
     */
    public User(String username,
                String password,
                String email) {
        this(username, password, email, DEFAULT_YEAR, DEFAULT_MAJOR, DEFAULT_TYPE);
    }

    /**
     * Generates a user from non-SQL Objects, assuming nothing
     *
     * @param username Username of User
     * @param password User's Password
     * @param email User's Email
     * @param year User's Year
     * @param major User's Major
     * @param type User's Type
     */
    public User(String username,
                String password,
                String email,
                UserYear year,
                String major,
                UserType type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.year = year;
        this.major = major;
        this.userType = type;
    }

    /**
     * Converts either 'A' or 'S' into an appropriate UserType that the constructors will
     * understand
     *
     * @param c Either 'A' for Admin or 'S' for Student
     * @return UserType representing SQL User_type
     */
    private UserType typeFromChar(char c) {
        return UserType.fromChar(c);
    }

    /**
     * Converts either 0, 1, 2, or 3 into respective UserYear object
     *
     * @param year 0 for Freshman, 1 for Sophomore, 2 for Junior, 3 for Senior
     * @return UserYear object representing class status
     */
    private UserYear yearFromInt(int year) {
        return UserYear.fromInt(year);
    }

    /**
     * Returns Username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns Email
     * @return Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns UserYear representing User's Year
     * @return Year
     */
    public UserYear getYear() {
        return year;
    }

    /**
     * Returns Major
     * @return Major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Returns UserType representing User's Admin/Student Status
     * @return UserType
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Returns a String that perfectly represents this object for a DB INSERT INTO statement
     * @return INSERT INTO statement representing this user
     */
    public String getInsertQuery() {
        return String.format("INSERT INTO User VALUES ('%s', '%s', '%s', %d, '%s', '%s');",
                             username, password, email,
                             year.getYear(), major, userType.getTypeChar());
    }

    /**
     * Returns a String that perfectly updates every attribute of this object in the DB
     * @return Complete UPDATE SET ... Statement representing this object
     */
    public String getUpdateQuery() {
        return String.format("UPDATE User SET Password='%s', Email='%s', Year=%d, "
                            + "Major='%s', UserType='%s' WHERE Username='%s';",
                             password, email, year.getYear(),
                             major, userType.getTypeChar(), username);
    }

    /**
     * Returns a String that selectively updates User Attributes in DB
     *
     * @param attributes Map of all attributes to update in DB, along with a String representation
     *                   of their values.
     * @return Perfect UPDATE SET ... statement representing desired change to User in DB
     */
    public String getUpdateQuery(Map<UserColumn, String> attributes) {
        String   query        = "UPDATE User SET ";
        Iterator<Map.Entry<UserColumn, String>> attrIterator = attributes.entrySet().iterator();
        while (attrIterator.hasNext()) {
            Map.Entry<UserColumn, String> entry = attrIterator.next();
            String formatString = "%s='%s'";
            String keyString = entry.getKey().getName();
            String valString = entry.getValue();
            if (entry.getKey() == UserColumn.YEAR) {
                formatString = "%s=%s";
                valString = "" + UserYear.fromName(valString).getYear();
            }
            query += String.format(formatString, keyString, valString);
            if (attrIterator.hasNext()) {
                query += ", ";
            }
        }
        query += String.format(" WHERE %s='%s';", UserColumn.USERNAME.getName(), username);
        return query;
    }

    public void apply(String projectName) throws Exception {
        Connection sql  = new SQLConnector().connect();
        Statement  stmt = sql.createStatement();
        ResultSet rs = stmt.executeQuery(
                String.format("SELECT * FROM Project WHERE Name='%s';", projectName)
        );
        Project p = null;
        while (rs.next()) {
            if (rs.getString("Name").equals(projectName)) {
                p = new Project(rs, rs.getRow());
            }
        }

        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (sql != null) sql.close();

        p.apply(this);
    }
}
