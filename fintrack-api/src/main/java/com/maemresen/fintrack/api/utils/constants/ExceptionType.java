package com.maemresen.fintrack.api.utils.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Service Error Definitions.
 * Each Error type has a group and a codeNumber
 * An error represented to the consumer of this API az foolows;
 * group + codeNumber
 * <p>
 * e.g.
 * group=E00, codeNumber=001
 * then error code will be E00-001
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum ExceptionType {

    // COMMON
    UNEXPECTED(ExceptionType.COMMON, "001", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAMETER(ExceptionType.COMMON, "002", HttpStatus.BAD_REQUEST),
    NOT_FOUND(ExceptionType.COMMON, "002", HttpStatus.NOT_FOUND),

    // AUTH
    ;

    private static final String COMMON = "E00";

    private final String group;
    private final String codeNumber;

    @Getter
    private final HttpStatus httpStatus;

    public String getCode() {
        return String.format("%s-%s", group, codeNumber);
    }

    @Override
    public String toString() {
        return getCode();
    }
}
