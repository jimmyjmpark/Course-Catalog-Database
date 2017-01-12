package edu.gatech.g73.app.user;

import edu.gatech.g73.db.SQLConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Singleton class, responsible for keeping track of the User logged in user
 * Also responsible for searching DB for a given User.
 *
 * Created by someone on 12/1/16.
 */
public class UserManager {
    
    private static User loggedInUser;

    /** Sets the Logged In User */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /** Returns the Currently Logged-in User */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /** Checks to see if a user is currently logged in */
    public static boolean isLoggedIn() {
        return loggedInUser == null;
    }

    /** Fetches User from DB and tries to log user in if valid credentials are supplied
     *
     * @param username Username of desired user
     * @param password Password of desired user
     * @return success status
     * @throws Exception Exception thrown when unable to connect to DB
     */
    public static boolean logInUser(String username, String password) throws Exception {
        User user = null;
        try {
            user = getUserWithUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null && user.getPassword().equals(password)) {
            loggedInUser = user;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fetches a User from the DB with the desired Username
     * @param username Username of desired user
     * @return User with username if found in DB, null if user is not found.
     * @throws Exception thrown when unable to connect to DB
     */
    public static User getUserWithUsername(String username) throws Exception {
        SQLConnector sql        = new SQLConnector();
        Connection   connection = sql.connect();
        Statement    statement  = connection.createStatement();
        ResultSet result     = statement.executeQuery(
                String.format("SELECT * FROM User WHERE %s = '%s';",
                              UserColumn.USERNAME, username)
        );
        User user = null;
        while (result.next()) {
            User candidate = new User(result, result.getRow());
            if (result.getString("Username").equals(candidate.getUsername())) {
                user = candidate;
                break;
            }
        }

        if (result != null) result.close();
        if (statement != null) statement.close();
        return user;
    }
}
