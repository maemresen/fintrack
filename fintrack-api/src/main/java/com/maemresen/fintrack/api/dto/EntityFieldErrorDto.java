package com.maemresen.fintrack.api.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@FieldNameConstants(level = AccessLevel.PRIVATE)
@JsonDeserialize(using = EntityFieldErrorDto.EntityFieldErrorDtoDeserializer.class)
public class EntityFieldErrorDto {
    private final String fieldClass;
    private final String field;
    private final String message;
    private final Object rejectedValue;
    private EntityFieldErrorDto(String fieldClass, String field, String message, Object rejectedValue) {
        this.fieldClass = fieldClass;
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public static InitialBuilder builder() {
        return new Builder();
    }

    public interface InitialBuilder {
        FieldSetter fieldClass(String fieldClass);
    }

    public interface FieldSetter {
        FieldSetter field(String field);

        FieldSetter message(String message);

        FieldSetter rejectedValue(Object rejectedValue);

        InitialBuilder and();

        List<EntityFieldErrorDto> build();
    }

    public static class EntityFieldErrorDtoDeserializer extends JsonDeserializer<EntityFieldErrorDto> {
        @Override
        public EntityFieldErrorDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new EntityFieldErrorDto(
                    node.get(Fields.fieldClass).asText(),
                    node.get(Fields.field).asText(),
                    node.get(Fields.message).asText(),
                    node.get(Fields.rejectedValue));
        }
    }

    public static class Builder implements InitialBuilder, FieldSetter {

        private final List<EntityFieldErrorDto> errors = new ArrayList<>();

        private boolean isUnsaved = false;
        private String currentFieldClass;
        private String currentField;
        private String currentMessage;
        private Object currentRejectedValue;

        @Override
        public FieldSetter fieldClass(String fieldClass) {
            this.isUnsaved = true;
            this.currentFieldClass = fieldClass;
            return this;
        }

        @Override
        public FieldSetter field(String field) {
            this.isUnsaved = true;
            this.currentField = field;
            return this;
        }

        @Override
        public FieldSetter message(String message) {
            this.isUnsaved = true;
            this.currentMessage = message;
            return this;
        }

        @Override
        public FieldSetter rejectedValue(Object rejectedValue) {
            this.isUnsaved = true;
            this.currentRejectedValue = rejectedValue;
            return this;
        }

        @Override
        public InitialBuilder and() {
            if (this.isUnsaved) {
                save();
            }

            return this;
        }

        @Override
        public List<EntityFieldErrorDto> build() {
            if (this.isUnsaved) {
                save();
            } else if (errors.isEmpty()) {
                throw new IllegalStateException("At least one error should be added before building the list.");
            }
            return new ArrayList<>(errors);
        }

        private void save() {
            EntityFieldErrorDto error = new EntityFieldErrorDto(currentFieldClass, currentField, currentMessage, currentRejectedValue);
            errors.add(error);

            this.isUnsaved = false;
            this.currentFieldClass = null;
            this.currentField = null;
            this.currentMessage = null;
            this.currentRejectedValue = null;

        }
    }
}
