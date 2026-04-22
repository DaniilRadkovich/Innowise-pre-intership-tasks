package com.radkovich.serializationlibrary;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class JsonSerializer {

    private boolean prettyPrint = false;

    public String serialize(Object object) {
        Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        visited.clear();
        return processValue(object, 0, visited);
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    private String processValue(Object object, int index, Set<Object> visited) {
        if (object == null) return "null";

        Class<?> clazz = object.getClass();

        if (visited.contains(object)) {
            throw new SerializationException("Cyclical reference detected!");
        }

        if (isPrimitiveOrWrapper(clazz) || object instanceof String) {
            return processPrimitive(object);
        }

        if (object instanceof Collection) {
            return processCollection((Collection<?>) object, index, visited);
        }

        if (object instanceof Map) {
            return processMap((Map<?, ?>) object, index, visited);
        }

        if (clazz.isArray()) {
            return processArray(object, index, visited);
        }

        visited.add(object);
        return processObject(object, index, visited);
    }

    private String processPrimitive(Object object) {
        if (object instanceof String ||
                object instanceof Character ||
                object instanceof Enum<?>) {
            return "\"" + object + "\"";
        }
        return String.valueOf(object);
    }

    private String processCollection(Collection<?> collection, int index, Set<Object> visited) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Object object : collection) {
            if (!first) sb.append(",");
            first = false;

            sb.append(processValue(object, index + 1, visited));
        }

        sb.append("]");
        return sb.toString();
    }

    private String processMap(Map<?, ?> object, int index, Set<Object> visited) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<?, ?> entry : object.entrySet()) {
            if (!first) sb.append(",");
            first = false;

            sb.append("\"").append(entry.getKey()).append("\": ");
            sb.append(processValue(entry.getValue(), index + 1, visited));
        }

        sb.append("}");
        return sb.toString();
    }

    private String processArray(Object array, int index, Set<Object> visited) {
        StringBuilder sb = new StringBuilder("[");
        int length = Array.getLength(array);

        for (int i = 0; i < length; i++) {
            if (i > 0) sb.append(",");
            sb.append(processValue(Array.get(array, i), index + 1, visited));
        }

        sb.append("]");
        return sb.toString();
    }

    private String processObject(Object object, int index, Set<Object> visited) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        Field[] fields = object.getClass().getDeclaredFields();
        boolean first = true;

        for (Field field : fields) {

            if (Modifier.isStatic(field.getModifiers()) ||
                    Modifier.isTransient(field.getModifiers()) ||
                    field.isAnnotationPresent(Exclude.class)) {
                continue;
            }
            field.setAccessible(true);

            try {
                Object value = field.get(object);
                String fieldName = field.getName();

                if (field.isAnnotationPresent(JsonName.class)) {
                    fieldName = field.getAnnotation(JsonName.class).value();
                }

                if (!first) sb.append(",");
                first = false;

                appendIndent(sb, index + 1);

                sb.append("\"").append(fieldName).append("\": ");
                sb.append(processValue(value, index + 1, visited));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        appendIndent(sb, index);
        sb.append("}");
        return sb.toString();
    }

    private void appendIndent(StringBuilder sb, int indent) {
        if (!prettyPrint) return;

        sb.append("\n");
        sb.append("  ".repeat(indent));
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                Number.class.isAssignableFrom(clazz) ||
                clazz == Boolean.class ||
                clazz == Character.class ||
                clazz.isEnum() ||
                clazz == Class.class;
    }
}
