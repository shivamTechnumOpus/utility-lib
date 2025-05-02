package com.example.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

public class MetadataValidatorUtil {

    public static class FieldMetadata {
        private String fieldName;
        private String regexPattern;
        private Integer maxLength;
        private Integer minLength;
        private Boolean isRequired;
        private String dataType;

        // Getters and setters
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        public String getRegexPattern() { return regexPattern; }
        public void setRegexPattern(String regexPattern) { this.regexPattern = regexPattern; }
        public Integer getMaxLength() { return maxLength; }
        public void setMaxLength(Integer maxLength) { this.maxLength = maxLength; }
        public Integer getMinLength() { return minLength; }
        public void setMinLength(Integer minLength) { this.minLength = minLength; }
        public Boolean getIsRequired() { return isRequired; }
        public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }
        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }
    }

    public void validateEntity(Object entity, List<FieldMetadata> metadataList) throws Exception {
        Class<?> clazz = entity.getClass();

        for (FieldMetadata metadata : metadataList) {
            try {
                Field field = clazz.getDeclaredField(metadata.getFieldName());
                field.setAccessible(true);
                Object value = field.get(entity);

                // Required field check
                if (metadata.getIsRequired() && value == null) {
                    throw new IllegalArgumentException(
                            metadata.getFieldName() + " is required");
                }

                if (value != null) {
                    // String validations
                    if (value instanceof String stringValue) {
                        // Length validations
                        if (metadata.getMaxLength() != null &&
                                stringValue.length() > metadata.getMaxLength()) {
                            throw new IllegalArgumentException(
                                    metadata.getFieldName() + " exceeds max length of " +
                                            metadata.getMaxLength());
                        }

                        if (metadata.getMinLength() != null &&
                                stringValue.length() < metadata.getMinLength()) {
                            throw new IllegalArgumentException(
                                    metadata.getFieldName() + " is below min length of " +
                                            metadata.getMinLength());
                        }

                        // Regex validation
                        if (metadata.getRegexPattern() != null &&
                                !Pattern.matches(metadata.getRegexPattern(), stringValue)) {
                            throw new IllegalArgumentException(
                                    metadata.getFieldName() + " does not match pattern");
                        }
                    }

                    // Numeric validations
                    if (value instanceof Number numberValue) {
                        // Add range validations if needed
                    }
                }
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException(
                        "Field " + metadata.getFieldName() + " not found in entity", e);
            }
        }
    }
}