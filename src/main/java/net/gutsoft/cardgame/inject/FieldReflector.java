package net.gutsoft.cardgame.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class FieldReflector {

    public static List<Field> collectFieldsUpToClass(Class<?> clazz, Class<?> upperBound) {
        ArrayList<Field> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != upperBound) {
            result.addAll(asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return result;
    }

    public static List<Field> filterInjectableFields(List<Field> allFields) {
        ArrayList<Field> result = new ArrayList<>();
        for (Field field: allFields) {
            Inject annotation = field.getAnnotation(Inject.class);
            if (annotation != null) {
                result.add(field);
            }
        }
        return result;
    }
}
