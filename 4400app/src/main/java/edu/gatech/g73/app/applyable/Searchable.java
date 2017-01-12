package edu.gatech.g73.app.applyable;

import javafx.beans.property.StringProperty;

import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public interface Searchable {

    String selectAllQuery();
    String getType();
    String getName();
    StringProperty getNameValue();
    StringProperty getTypeValue();
    Map<String, String> exportValues();
}
