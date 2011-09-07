package org.jarbframework.populator.excel.mapping.importer;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jarbframework.populator.excel.DefaultExcelTestDataCase;
import org.jarbframework.populator.excel.mapping.importer.ExcelImporter;
import org.jarbframework.populator.excel.mapping.importer.ExcelRow;
import org.jarbframework.populator.excel.mapping.importer.ForeignRelationsMapper;
import org.jarbframework.populator.excel.mapping.importer.Key;
import org.jarbframework.populator.excel.metamodel.EntityDefinition;
import org.jarbframework.populator.excel.metamodel.PropertyDefinition;
import org.jarbframework.populator.excel.workbook.Workbook;
import org.junit.Before;
import org.junit.Test;

public class ForeignRelationsMapperTest extends DefaultExcelTestDataCase {

    private Workbook excel;

    @Before
    public void setUpExcelRecordTest() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException, InvalidFormatException,
            IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        excel = getExcelDataManagerFactory().buildExcelParser().parse(new FileInputStream("src/test/resources/ExcelUnitTesting.xls"));

        //For code coverage purposes:
        Constructor<ForeignRelationsMapper> constructor = ForeignRelationsMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /* TO FIX: Crashes if not equal to mapping */
    @Test
    public void testMakeForeignRelations() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidFormatException, IOException,
            SecurityException, NoSuchFieldException {
        excel = getExcelDataManagerFactory().buildExcelParser().parse(new FileInputStream("src/test/resources/Excel.xls"));

        Map<EntityDefinition<?>, Map<Object, ExcelRow>> objectModel = ExcelImporter.parseExcel(excel, generateMetamodel().entities());

        for (Entry<EntityDefinition<?>, Map<Object, ExcelRow>> classRecord : objectModel.entrySet()) {
            for (Entry<Object, ExcelRow> classValues : classRecord.getValue().entrySet()) {
                ExcelRow excelRow = classValues.getValue();
                Class<?> tobeTested = domain.entities.Project.class;
                if (excelRow.getCreatedInstance().getClass() == tobeTested) {
                    ForeignRelationsMapper.makeForeignRelations(excelRow, objectModel);
                    // excelRow.getValueMap().containsValue("JoinColumnKey");
                    for (Entry<PropertyDefinition, Key> entry : excelRow.getValueMap().entrySet()) {
                        assertEquals(domain.entities.Customer.class, entry.getValue().getForeignClass());
                    }
                }
            }

        }
    }

}