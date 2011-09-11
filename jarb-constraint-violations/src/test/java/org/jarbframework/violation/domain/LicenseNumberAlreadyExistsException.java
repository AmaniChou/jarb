package org.jarbframework.violation.domain;

import org.jarbframework.violation.DatabaseConstraintViolation;
import org.jarbframework.violation.UniqueKeyViolationException;
import org.jarbframework.violation.factory.DatabaseConstraintViolationExceptionFactory;

/**
 * Car license number can only be used once.
 * 
 * @author Jeroen van Schagen
 * @since 27-05-2011
 */
public class LicenseNumberAlreadyExistsException extends UniqueKeyViolationException {
    private static final long serialVersionUID = 8122213238219945641L;
    private DatabaseConstraintViolationExceptionFactory factory;
    
    public LicenseNumberAlreadyExistsException(DatabaseConstraintViolation violation) {
        super(violation);
    }

    public LicenseNumberAlreadyExistsException(DatabaseConstraintViolation violation, Throwable cause) {
        super(violation, cause);
    }

    public LicenseNumberAlreadyExistsException(DatabaseConstraintViolation violation, Throwable cause, DatabaseConstraintViolationExceptionFactory factory) {
        super(violation, cause);
        this.factory = factory;
    }

    /**
     * Retrieve the factory that generated this exception.
     * @return exception factory
     */
    public DatabaseConstraintViolationExceptionFactory getExceptionFactory() {
        return factory;
    }

}