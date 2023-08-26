package com.maemresen.fintrack.api.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UriConstant {
    private static final String BLANK_URI = "";

    @UtilityClass
    public static final class Budget {
        public static final String BASE_URI = "budget";
        public static final String CREATE = BLANK_URI;
        public static final String FIND_BY_ID = "{budgetId}";
        public static final String FIND_ALL = BLANK_URI;
        public static final String ADD_STATEMENT = "{budgetId}/statement";
        public static final String REMOVE_STATEMENT = "{budgetId}/statement/{statementId}";
        public static final String MONTHLY_REPORT_FOR_YEAR = "{budgetId}/report/{year}";
    }
}
