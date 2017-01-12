package edu.gatech.g73.app.applyable.course;

import edu.gatech.g73.app.applyable.project.ProjectColumn;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public enum CourseColumn {

    NAME("Name"), COURSE_NUMBER("Course_number"), INSTRUCTOR("Instructor"),
    EST_STUDENTS("Est_students"), DESIGNATION_NAME("Designation_name");

    // http://stackoverflow.com/questions/5292790/convert-integer-value-to-matching-java-enum
    private static final Map<String, CourseColumn>  map;
    private              String                     name;

    CourseColumn(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    static {
        map = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name, e -> e));
    }

    /**
     * Converts String Column Name into appropriate Enum
     * @param val Appropriate Column Name
     * @return representation of Column Name if found, defaults to NAME
     */
    public static CourseColumn fromName(String val) {
        return Optional.ofNullable(map.get(val)).orElse(CourseColumn.NAME);
    }
}
