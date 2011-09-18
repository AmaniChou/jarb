package org.jarbframework.violation.factory.custom;

import org.jarbframework.violation.DatabaseConstraintViolation;

/**
 * Determines if a constraint violation matches the expression.
 * @author Jeroen van Schagen
 * @since Aug 31, 2011
 */
public interface ConstraintViolationMatchingStrategy {

    /**
     * Determine if a specific constraint name matches a constraint expression.
     * @param violation database constraint violation being matched
     * @param expression expression being matched against
     * @return {@code true} if the violation matches our expression, else {@code false}
     */
    boolean matches(DatabaseConstraintViolation violation, String expression);

}