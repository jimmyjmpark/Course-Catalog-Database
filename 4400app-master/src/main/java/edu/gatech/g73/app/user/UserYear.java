package edu.gatech.g73.app.user;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Year is a representation of all of the possible values for year that the database will accept
 * in the `User` table
 *
 * Created by someone on 12/2/16.
 */
public enum UserYear {
    
    FRESHMAN(1, "Freshman"), SOPHOMORE(2, "Sophomore"),
    JUNIOR(3, "Junior"), SENIOR(4, "Senior");
    
    private int year;
    private String name;
    
    UserYear(int year, String name) {
        this.year = year;
        this.name = name;
    }
    
    /**
     * Fetch the database-accepted value for the user's year
     * @return
     */
    public int getYear() {
        return year;
    }

    /** Return Year Standing (Freshman, Sophomore, etc) */
    public String getName() { return name; }
    
    // http://stackoverflow.com/questions/5292790/convert-integer-value-to-matching-java-enum
    private static final Map<Integer, UserYear> numberMap;
    static {
        numberMap = Arrays.stream(values()).collect(Collectors.toMap(e -> e.year, e -> e));
    }

    /** Converts a DB-Supplied int into a representative UserYear */
    public static UserYear fromInt(int val) {
        return Optional.ofNullable(numberMap.get(val)).orElse(UserYear.FRESHMAN);
    }

    private static final Map<String, UserYear> nameMap;
    static {
        nameMap = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name, e -> e));
    }

    /** Converts an App-supplied year standing (Freshman, Sophomore, etc.) into UserYear */
    public static UserYear fromName(String val) {
        return Optional.ofNullable(nameMap.get(val)).orElse(UserYear.FRESHMAN);
    }
    
}
