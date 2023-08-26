package com.maemresen.fintrack.api.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UriConstant {

    private static final String BLANK_URI = "";

    @UtilityClass
    public static final class Budget {
        public static final String BASE_URI = "budget";
        public static final String CREATE_URI = BLANK_URI;
        public static final String FIND_BY_ID_URI = "{budgetId}";
        public static final String FIND_ALL_URI = BLANK_URI;
        public static final String ADD_STATEMENT_URI = "{budgetId}/statement";
        public static final String REMOVE_STATEMENT_URI = "{budgetId}/statement/{statementId}";
        public static final String MONTHLY_REPORT_FOR_YEAR_URI = "{budgetId}/report/{year}";
    }

    @UtilityClass
    public static final class ErrorCode {
        public static final String BASE_URI = "error-code";
        public static final String FIND_ALL_URI = BLANK_URI;
    }
}
