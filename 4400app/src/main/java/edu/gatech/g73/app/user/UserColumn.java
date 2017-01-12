package edu.gatech.g73.app.user;

/**
 * Represents All of the possible columns in the User table in the DB
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/3/16
 */
public enum UserColumn {

    USERNAME("Username"), PASSWORD("Password"), EMAIL("Email"), YEAR("Year"), MAJOR("Major"),
    USER_TYPE("UserType");

    private String name;

    UserColumn(String name) {
        this.name = name;
    }

    /**
     * Returns the column name of the enum that can be fed directly into DB query
     * @return Column Name in DB
     */
    public String getName() {
        return name;
    }

}
