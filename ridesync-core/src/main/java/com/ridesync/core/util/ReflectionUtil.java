package com.ridesync.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class demonstrating reflection capabilities
 * Used for metadata inspection and debugging
 */
public class ReflectionUtil {
    
    /**
     * Get metadata about a class including fields and methods
     * 
     * @param clazz Class to inspect
     * @return Map containing class metadata
     */
    public static Map<String, Object> getClassMetadata(Class<?> clazz) {
        Map<String, Object> metadata = new HashMap<>();
        
        metadata.put("className", clazz.getSimpleName());
        metadata.put("packageName", clazz.getPackageName());
        metadata.put("superClass", clazz.getSuperclass() != null ? 
            clazz.getSuperclass().getSimpleName() : "None");
        
        // Get fields using reflection
        String[] fields = Arrays.stream(clazz.getDeclaredFields())
            .map(Field::getName)
            .toArray(String[]::new);
        metadata.put("fields", fields);
        
        // Get methods using reflection
        String[] methods = Arrays.stream(clazz.getDeclaredMethods())
            .map(Method::getName)
            .distinct()
            .toArray(String[]::new);
        metadata.put("methods", methods);
        
        // Get interfaces
        String[] interfaces = Arrays.stream(clazz.getInterfaces())
            .map(Class::getSimpleName)
            .toArray(String[]::new);
        metadata.put("interfaces", interfaces);
        
        return metadata;
    }
    
    /**
     * Get all field names and types of a class
     */
    public static Map<String, String> getFieldTypes(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .collect(Collectors.toMap(
                Field::getName,
                field -> field.getType().getSimpleName()
            ));
    }
}
