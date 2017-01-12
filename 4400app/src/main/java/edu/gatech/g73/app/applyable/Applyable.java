package edu.gatech.g73.app.applyable;

import edu.gatech.g73.app.user.User;

/**
 * <CLASS_NAME> is what?
 *
 * @author Andrew Ray
 * @version 0.0.0
 * @since 12/4/16
 */
public interface Applyable extends Searchable {

    boolean canApply(User user);
    void apply() throws Exception;
    void apply(User user) throws Exception;
}
