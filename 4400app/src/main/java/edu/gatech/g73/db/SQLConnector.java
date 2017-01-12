package edu.gatech.g73.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/2/16
 */
public class SQLConnector {

    public static final Map<String, String> DEFAULT_OPTIONS = new HashMap<String, String>() {{
        put("username", "cs4400_Team_73");
        put("password", "dRyvBfox");
        put("host", "academic-mysql.cc.gatech.edu");
        put("db", "cs4400_Team_73");
    }};

    private String username;
    private String password;
    private String host;
    private String db;

    private Connection connection;
    private Statement statement;
    private ResultSet result;

    public SQLConnector() {
        setOptions(DEFAULT_OPTIONS);
    }

    public void setOptions(Map<String, String> options) {
        for (Map.Entry<String, String> entry : options.entrySet()) {
            switch (entry.getKey()) {
                case "username": username = entry.getValue();
                                 break;
                case "password": password = entry.getValue();
                                 break;
                case "host":     host = entry.getValue();
                                 break;
                case "db":       db = entry.getValue();
                                 break;
                default:         break;
            }
        }
    }

    public Connection connect() throws Exception {
        return DriverManager.getConnection(
                String.format("jdbc:mysql://%s:3306/%s?user=%s&password=%s&autoReconnect=true"
                              + "&useSSL=false",
                              host, db, username, password));
    }
}
