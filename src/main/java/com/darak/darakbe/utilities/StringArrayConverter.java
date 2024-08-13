package com.darak.darakbe.utilities;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
@Converter(autoApply = true)
public class StringArrayConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null) {
            return null;
        }
        return String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        return List.of(dbData.split(","));
    }
}
