package com.maemresen.fintrack.util.test;

import org.assertj.core.api.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseEntityAssert<T> extends AbstractAssert<ResponseEntityAssert<T>, ResponseEntity<T>> {
    public static <T> ResponseEntityAssert<T> assertResponseEntity(ResponseEntity<T> actual) {
        return new ResponseEntityAssert<>(actual);
    }

    protected ResponseEntityAssert(ResponseEntity<T> responseEntity) {
        super(responseEntity, ResponseEntityAssert.class);
    }

    public ResponseEntityAssert<T> isStatus2xx() {
        isNotNull();
        assertThat(actual.getStatusCode()).matches(HttpStatusCode::is2xxSuccessful);
        return this;
    }

    public ResponseEntityAssert<T> containsBody() {
        isNotNull();
        assertThat(actual.getBody()).isNotNull();
        return this;
    }
}
