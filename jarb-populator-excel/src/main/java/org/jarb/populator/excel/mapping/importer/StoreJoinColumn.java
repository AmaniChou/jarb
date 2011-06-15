package org.jarb.populator.excel.mapping.importer;

import org.jarb.populator.excel.mapping.excelrow.ExcelRow;
import org.jarb.populator.excel.mapping.excelrow.JoinColumnKey;
import org.jarb.populator.excel.mapping.excelrow.Key;
import org.jarb.populator.excel.metamodel.ClassDefinition;
import org.jarb.populator.excel.metamodel.ColumnDefinition;
import org.jarb.populator.excel.workbook.Sheet;
import org.jarb.populator.excel.workbook.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores the value of a JoinColumn.
 * @author Sander Benschop
 * @author Willem Eppen
 *
 */
public final class StoreJoinColumn {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreJoinColumn.class);

    /** Private constructor. */
    private StoreJoinColumn() {
    }

    /**
     * Stores a JoinColumn in ExcelRow.
     * @param excel Representation of excel file
     * @param classDefinition ClassDefinition used to determine columnPosition
     * @param columnDefinition ColumnDefinition is the superclass of Column, JoinColumn and JoinTable.
     * @param rowPosition Vertical position number of the excelRecord
     * @param excelRow ExcelRow to save to.
     */
    public static void storeValue(Workbook excel, ClassDefinition<?> classDefinition, ColumnDefinition columnDefinition, Integer rowPosition, ExcelRow excelRow) {
        Sheet sheet = excel.getSheet(classDefinition.getTableName());
        Object cellValue = sheet.getCellValueAt(rowPosition, columnDefinition.getColumnName());
        if(cellValue instanceof Double) {
            LOGGER.debug("field: " + columnDefinition.getFieldName() + " column: " + columnDefinition.getColumnName() + " value:[" + cellValue + "]");
            if (cellValue != null) {
                // Sets the Key
                Key keyValue = new JoinColumnKey();
                keyValue.setKeyValue(((Double) cellValue).intValue());
                if(columnDefinition.getField() == null) {
                    String columnName = columnDefinition.getColumnName();
                    System.out.println(columnName);
                }
                keyValue.setForeignClass(columnDefinition.getField().getType());
                excelRow.addValue(columnDefinition, keyValue);
            }
        }
    }
}
