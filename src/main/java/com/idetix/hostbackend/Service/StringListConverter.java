package com.idetix.hostbackend.Service;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (stringList != null && !stringList.isEmpty()) {
            return String.join(SPLIT_CHAR, stringList);
        } else return null;
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if (string != null) {
            return Arrays.asList(string.split(SPLIT_CHAR));
        } else return null;
    }
}