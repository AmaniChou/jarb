package org.jarbframework.constraint.violation.resolver;

import java.util.Collection;
import java.util.LinkedList;

import org.jarbframework.constraint.violation.DatabaseConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chain of responsiblity for constraint violation resolvers. Whenever a violation
 * resolver could not resolve the violation, our next resolver is invoked. This
 * process continues until a valid constraint violation gets resolved, or we run
 * out of violation resolvers.
 * 
 * @author Jeroen van Schagen
 * @since 16-05-2011
 */
public class ViolationResolverChain implements DatabaseConstraintViolationResolver {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Collection of violation resolvers that should be attempted in sequence.
     */
    private final Collection<DatabaseConstraintViolationResolver> violationResolvers;

    /**
     * Construct a new chain of violation resolvers.
     */
    public ViolationResolverChain() {
        violationResolvers = new LinkedList<DatabaseConstraintViolationResolver>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseConstraintViolation resolve(Throwable throwable) {
        for (DatabaseConstraintViolationResolver violationResolver : violationResolvers) {
            logger.debug("Attempting to resolve violation with {}", violationResolver.getClass());
            DatabaseConstraintViolation violation = violationResolver.resolve(throwable);
            if (violation != null) {
                return violation;
            }
        }
        
        return null;
    }

    /**
     * Add a violation resolver to the back of this chain.
     * @param resolver violation resolver instance we are adding
     * @return {@code this} instance, enabling the use of method chaining
     */
    public ViolationResolverChain addToChain(DatabaseConstraintViolationResolver resolver) {
        if(resolver != null) {
            violationResolvers.add(resolver);
        }
        return this;
    }
    
}
