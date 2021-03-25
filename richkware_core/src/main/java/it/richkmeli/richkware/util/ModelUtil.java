package it.richkmeli.richkware.util;

import java.lang.reflect.Field;

public class ModelUtil {

    public static <MODEL> String getURLWithParameters(String service, MODEL model) {

        //TODO to be tested
        StringBuilder stringBuilder = new StringBuilder();
        boolean oneFieldinitialized = false;
        //for (Field field : this.getClass().getFields()) {
        for (Field field : model.getClass().getFields()) {
            if (field != null) {
                oneFieldinitialized = true;
                stringBuilder.append(field == null ? "" : field.toString());
            }
        }

        if (oneFieldinitialized)
            return service + "?" + stringBuilder.toString();
        else
            return service;
    }

}
