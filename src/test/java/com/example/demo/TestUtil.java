package com.example.demo;

import java.lang.reflect.Field;

public class TestUtil {

    @SuppressWarnings("deprecation")
    public static void injectObject(Object target, String fieldName, Object toInject) throws NoSuchFieldException, IllegalAccessException {

        boolean wasPrivate = false;

        Field declaredField = target.getClass().getDeclaredField(fieldName);
        if (!declaredField.isAccessible()) {
            declaredField.setAccessible(true);
            wasPrivate = true;
        }
        declaredField.set(target, toInject);
        if (wasPrivate)
            declaredField.setAccessible(false);

    }
}
