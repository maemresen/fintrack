package com.maemresen.fintrack.webservice.common;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.lang.reflect.Field;

public class CustomMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    @Override
    public String getColumnName(int col) {
        String columnName = super.getColumnName(col);
        return getFieldNameBasedOnHeader(columnName);
    }

    private String getFieldNameBasedOnHeader(String headerName) {
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvHeader.class)) {
                CsvHeader annotation = field.getAnnotation(CsvHeader.class);
                if (annotation.value().equals(headerName)) {
                    return field.getName();
                }
            }
        }
        return headerName; // Fallback to default behavior
    }
}
