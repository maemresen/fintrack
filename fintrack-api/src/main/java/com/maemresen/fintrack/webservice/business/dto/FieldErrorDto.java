package com.maemresen.fintrack.webservice.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@JsonDeserialize(using = FieldErrorDto.EntityFieldErrorDtoDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldErrorDto {

    private String fieldClass;
    private String field;
    private String message;
    private Object rejectedValue;

    public static FieldErrorDto withFieldClass(@NotNull Class<?> fieldClass, @NotNull String field, String message, Object rejectedValue) {
        return new FieldErrorDto(fieldClass.getName(), field, message, rejectedValue);
    }

    public static FieldErrorDto withField(@NotNull String field, String message, Object rejectedValue) {
        return new FieldErrorDto(null, field, message, rejectedValue);
    }

    @JsonIgnore
    public String getStringRejectedValue() {
        return this.getOptionalRejectedValueAsString().orElse(null);
    }

    @JsonIgnore
    public Integer getIntRejectedValue() {
        return this.getRejectedValueAs(Integer::parseInt);
    }

    @JsonIgnore
    public Long getLongRejectedValue() {
        return this.getRejectedValueAs(Long::parseLong);
    }

    @JsonIgnore
    public Double getDoubleRejectedValue() {
        return this.getRejectedValueAs(Double::parseDouble);
    }

    @JsonIgnore
    public Boolean getBooleanRejectedValue() {
        return this.getRejectedValueAs(Boolean::parseBoolean);
    }

    private <T> T getRejectedValueAs(Function<String, T> mapper) {
        return this.getOptionalRejectedValueAsString().map(mapper).orElse(null);
    }

    private Optional<String> getOptionalRejectedValueAsString() {
        return Optional.ofNullable(rejectedValue)
            .map(Object::toString);
    }


    public static class EntityFieldErrorDtoDeserializer extends JsonDeserializer<FieldErrorDto> {
        @Override
        public FieldErrorDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            FieldErrorDto fieldErrorDto = new FieldErrorDto();
            JsonNode node = p.getCodec().readTree(p);

            Optional.ofNullable(node.get(Fields.fieldClass))
                    .map(JsonNode::asText)
                    .ifPresent(fieldErrorDto::setFieldClass);

            Optional.ofNullable(node.get(Fields.field))
                    .map(JsonNode::asText)
                    .ifPresent(fieldErrorDto::setField);

            Optional.ofNullable(node.get(Fields.message))
                    .map(JsonNode::asText)
                    .ifPresent(fieldErrorDto::setMessage);

            Optional.ofNullable(node.get(Fields.rejectedValue))
                    .ifPresent(fieldErrorDto::setRejectedValue);

            return fieldErrorDto;
        }
    }
}
