package edu.gatech.g73.app.user;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by someone on 12/2/16.
 */
public enum UserType {
    
    STUDENT('S'), ADMIN('A');
    
    private char dbVal;
    
    UserType(char dbVal) {
        this.dbVal = dbVal;
    }

    /**
     * Fetches the DB-accepted value for this enum
     * @return Representation of User's Admin Status
     */
    public char getTypeChar() {
        return dbVal;
    }
    
    // http://stackoverflow.com/questions/5292790/convert-integer-value-to-matching-java-enum
    private static final Map<Character, UserType> map;
    static {
        map = Arrays.stream(values()).collect(Collectors.toMap(e -> e.dbVal, e -> e));
    }

    /**
     * Converts either 'A' or 'S' into its representative UserType
     * @param val User Type of either 'A' or 'S'
     * @return representation of User's Admin Status/Type
     */
    public static UserType fromChar(char val) {
        return Optional.ofNullable(map.get(val)).orElse(UserType.STUDENT);
    }
}
