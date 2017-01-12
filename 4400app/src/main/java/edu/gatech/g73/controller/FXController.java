package edu.gatech.g73.controller;

import java.util.Map;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/3/16
 */
public interface FXController<K, V> {

    void initialize();
    void initData(Map<K, V> info);
}
