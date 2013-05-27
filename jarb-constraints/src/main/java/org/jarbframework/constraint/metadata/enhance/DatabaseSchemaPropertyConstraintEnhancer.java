package org.jarbframework.constraint.metadata.enhance;

import static org.jarbframework.utils.Asserts.notNull;

import org.jarbframework.constraint.metadata.PropertyConstraintDescription;
import org.jarbframework.constraint.metadata.database.ColumnMetadata;
import org.jarbframework.constraint.metadata.database.ColumnMetadataRepository;
import org.jarbframework.utils.bean.PropertyReference;
import org.jarbframework.utils.orm.ColumnReference;
import org.jarbframework.utils.orm.SchemaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enhances the property description with database constraint information.
 * 
 * @author Jeroen van Schagen
 * @since 31-05-2011
 */
public class DatabaseSchemaPropertyConstraintEnhancer implements PropertyConstraintEnhancer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final ColumnMetadataRepository columnMetadataRepository;

    private final SchemaMapper schemaMapper;

    public DatabaseSchemaPropertyConstraintEnhancer(ColumnMetadataRepository columnMetadataRepository, SchemaMapper schemaMapper) {
        this.columnMetadataRepository = notNull(columnMetadataRepository, "Column metadata repository cannot be null");
        this.schemaMapper = notNull(schemaMapper, "Schema mapper cannot be null");
    }

    @Override
    public PropertyConstraintDescription enhance(PropertyConstraintDescription propertyDescription) {
        PropertyReference propertyReference = propertyDescription.toPropertyReference();

        if (! schemaMapper.isEmbeddable(propertyDescription.getJavaType())) {
            ColumnReference columnReference = schemaMapper.getColumnReference(propertyReference);
            if (columnReference != null) {
                ColumnMetadata columnMetadata = columnMetadataRepository.getColumnMetadata(columnReference);
                if (columnMetadata != null) {
                    propertyDescription.setRequired(isValueRequired(columnMetadata));
                    propertyDescription.setMaximumLength(columnMetadata.getMaximumLength());
                    propertyDescription.setFractionLength(columnMetadata.getFractionLength());
                    propertyDescription.setRadix(columnMetadata.getRadix());
                } else {
                    logger.debug("Could not resolve column metadata for '{}'.", propertyReference);
                }
            }
        }

        return propertyDescription;
    }

    // When the column value can be generated by our database, it is not required
    private boolean isValueRequired(ColumnMetadata columnMetadata) {
        return columnMetadata.isRequired() && ! columnMetadata.isGeneratable();
    }

}