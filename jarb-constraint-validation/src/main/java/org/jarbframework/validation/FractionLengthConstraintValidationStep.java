package org.jarbframework.validation;

import java.math.BigDecimal;

import org.jarbframework.constraint.database.column.ColumnMetadata;
import org.jarbframework.utils.bean.PropertyReference;

public class FractionLengthConstraintValidationStep implements DatabaseConstraintValidationStep {
    private static final String FRACTION_LENGTH_TEMPLATE = "{org.jarb.validation.DatabaseConstraint.FractionLength.message}";

    @Override
    public void validate(Object propertyValue, PropertyReference propertyRef, ColumnMetadata columnMetadata, DatabaseConstraintValidationContext validation) {
        if (fractionLengthExceeded(propertyValue, columnMetadata)) {
            validation.buildViolationWithTemplate(propertyRef, FRACTION_LENGTH_TEMPLATE)
                          .attribute("max", columnMetadata.getFractionLength())
                          .value(propertyValue)
                              .addViolation();
        }
    }
    
    private boolean fractionLengthExceeded(Object propertyValue, ColumnMetadata columnMetadata) {
        boolean lengthExceeded = false;
        if (columnMetadata.hasFractionLength()) {
            if(propertyValue instanceof Number) {
                lengthExceeded = lengthOfFraction((Number) propertyValue) > columnMetadata.getFractionLength();
            }
        }
        return lengthExceeded;
    }

    private int lengthOfFraction(Number number) {
        BigDecimal numberAsBigDecimal = new BigDecimal(number.toString());
        return numberAsBigDecimal.scale() < 0 ? 0 : numberAsBigDecimal.scale();
    }
}
