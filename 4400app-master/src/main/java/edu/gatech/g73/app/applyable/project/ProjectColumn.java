package edu.gatech.g73.app.applyable.project;

import edu.gatech.g73.app.user.UserType;

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
public enum ProjectColumn {

    NAME("Name"), DESCRIPTION("Description"), ADVISOR_EMAIL("Advisor_email"),
    ADVISOR_NAME("Advisor_name"), EST_STUDENTS("Est_students"),
    DESIGNATION_NAME("Designation_name");

    // http://stackoverflow.com/questions/5292790/convert-integer-value-to-matching-java-enum
    private static final Map<String, ProjectColumn> map;
    private String name;

    ProjectColumn(String name) {
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
    public static ProjectColumn fromName(String val) {
        return Optional.ofNullable(map.get(val)).orElse(ProjectColumn.NAME);
    }
}
