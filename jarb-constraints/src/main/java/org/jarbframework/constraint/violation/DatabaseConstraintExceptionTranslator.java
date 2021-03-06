package org.jarbframework.constraint.violation;

import static org.jarbframework.utils.Asserts.notNull;

import org.jarbframework.constraint.violation.factory.DatabaseConstraintExceptionFactory;
import org.jarbframework.constraint.violation.factory.DefaultConstraintExceptionFactory;
import org.jarbframework.constraint.violation.resolver.DatabaseConstraintViolationResolver;

/**
 * Possibly translates database exceptions into, a more clear,
 * constraint violation exception. Whenever no translation could
 * be performed, we return the original exception.
 * 
 * @author Jeroen van Schagen
 * @since 17-05-2011
 */
public class DatabaseConstraintExceptionTranslator {
	
    /** Resolves the constraint violation from an exception. **/
    private final DatabaseConstraintViolationResolver violationResolver;
    
    /** Creates an exception for some constraint violation. **/
    private final DatabaseConstraintExceptionFactory exceptionFactory;

    /**
     * Construct a new {@link DatabaseConstraintExceptionTranslator}.
     * @param violationResolver resolves the constraint violation from an exception
     */
    public DatabaseConstraintExceptionTranslator(DatabaseConstraintViolationResolver violationResolver) {
        this(violationResolver, new DefaultConstraintExceptionFactory());
    }

    /**
     * Construct a new {@link DatabaseConstraintExceptionTranslator}.
     * @param violationResolver resolves the constraint violation from an exception
     * @param exceptionFactory creates an exception for some constraint violation
     */
    public DatabaseConstraintExceptionTranslator(DatabaseConstraintViolationResolver violationResolver, DatabaseConstraintExceptionFactory exceptionFactory) {
        this.violationResolver = notNull(violationResolver, "Violation resolver cannot be null.");
        this.exceptionFactory = notNull(exceptionFactory, "Exception factory cannot be null.");
    }

    /**
     * Attempt to translate an exception into a constraint violation exception.
     * @return a constraint violation exception, or {@code null} if no translation could be done
     */
    public Throwable translate(Throwable throwable) {
        Throwable translation = null;
        DatabaseConstraintViolation violation = violationResolver.resolve(throwable);
        if (violation != null) {
            translation = exceptionFactory.buildException(violation, throwable);
        }
        return translation;
    }

}
