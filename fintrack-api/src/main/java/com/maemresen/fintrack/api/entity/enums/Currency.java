package com.maemresen.fintrack.api.entity.enums;

public enum Currency {
    TRY, USD, EUR;

    public static Currency getDefaultValue() {
        return EUR;
    }
}
