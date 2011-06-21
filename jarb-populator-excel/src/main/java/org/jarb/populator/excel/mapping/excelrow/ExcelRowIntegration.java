package org.jarb.populator.excel.mapping.excelrow;

import java.util.HashMap;
import java.util.Map;

import org.jarb.populator.excel.entity.EntityRegistry;
import org.jarb.populator.excel.entity.EntityTable;
import org.jarb.populator.excel.metamodel.ClassDefinition;

/**
 * Temporary integration utility, used to interface with old components.
 * Eventually, our components should implement the new interface, meaning
 * this utility would no longer be necesairy.
 * @author Jeroen van Schagen
 * @since 11-05-2011
 */
public class ExcelRowIntegration {

    @SuppressWarnings("unchecked")
    public static EntityRegistry toRegistry(Map<ClassDefinition<?>, Map<Integer, ExcelRow>> entitiesMap) {
        EntityRegistry registry = new EntityRegistry();
        for (Map.Entry<ClassDefinition<?>, Map<Integer, ExcelRow>> entitiesEntry : entitiesMap.entrySet()) {
            final Class entityClass = entitiesEntry.getKey().getPersistentClass();
            EntityTable<Object> entities = new EntityTable<Object>(entityClass);
            for (Map.Entry<Integer, ExcelRow> excelRowEntry : entitiesEntry.getValue().entrySet()) {
                entities.add(excelRowEntry.getKey().longValue(), excelRowEntry.getValue().getCreatedInstance());
            }
            registry.addAll(entities);
        }
        return registry;
    }
        
    public static Map<Class<?>, Map<Integer, ExcelRow>> toMap(EntityRegistry registry) {
        Map<Class<?>, Map<Integer, ExcelRow>> entitiesMap = new HashMap<Class<?>, Map<Integer, ExcelRow>>();
        for (Class<?> entityClass : registry.getEntityClasses()) {
            Map<Integer, ExcelRow> excelRowMap = new HashMap<Integer, ExcelRow>();
            for (Map.Entry<Object, ?> entityEntry : registry.getAll(entityClass).map().entrySet()) {
                excelRowMap.put(((Long) entityEntry.getKey()).intValue(), new ExcelRow(entityEntry.getValue()));
            }
            entitiesMap.put(entityClass, excelRowMap);
        }
        return entitiesMap;
    }

    public static Map<Class<?>, Map<Integer, ExcelRow>> toMap(Map<ClassDefinition<?>, Map<Integer, ExcelRow>> entitiesMap) {
        Map<Class<?>, Map<Integer, ExcelRow>> classMap = new HashMap<Class<?>, Map<Integer, ExcelRow>>();
        for (Map.Entry<ClassDefinition<?>, Map<Integer, ExcelRow>> entitiesEntry : entitiesMap.entrySet()) {
            classMap.put(entitiesEntry.getKey().getPersistentClass(), entitiesEntry.getValue());
        }
        return classMap;
    }
}
