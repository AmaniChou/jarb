package org.jarb.populator.excel.mapping.exporter;

import org.jarb.populator.excel.entity.EntityRegistry;
import org.jarb.populator.excel.metamodel.ClassDefinition;
import org.jarb.populator.excel.metamodel.MetaModel;
import org.jarb.populator.excel.workbook.Sheet;
import org.jarb.populator.excel.workbook.Workbook;

/**
 * Default implementation of {@link EntityExporter}.
 * <b>Note that this component does not work yet!</b>
 * @author Jeroen van Schagen
 * @since 12-05-2011
 */
public class DefaultEntityExporter implements EntityExporter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Workbook export(EntityRegistry registry, MetaModel metamodel) {
        Workbook workbook = new Workbook();
        for (ClassDefinition<?> classDefinition : metamodel.getClassDefinitions()) {
            createEntitySheet(workbook, registry, classDefinition);
        }
        return workbook;
    }
    
    private <T> void createEntitySheet(Workbook workbook, EntityRegistry registry, ClassDefinition<T> classDefinition) {
        Sheet sheet = workbook.createSheet(classDefinition.getTableName());
        storeColumnNames(sheet, classDefinition);
        for(T entity : registry.getAll(classDefinition.getPersistentClass())) {
            System.out.println("TODO: Export " + entity + " to excel");
        }
    }
    
    private void storeColumnNames(Sheet sheet, ClassDefinition<?> classDefinition) {
        int columnNumber = 0;
        for(String columnName : classDefinition.getColumnNames()) {
            sheet.setColumnNameAt(columnNumber++, columnName);
        }
    }

}
