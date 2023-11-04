package com.maemresen.fintrack.webservice.business.entity.enums;

public enum Currency {
    TRY, USD, EUR;

    public static Currency getDefaultValue() {
        return EUR;
    }
}
