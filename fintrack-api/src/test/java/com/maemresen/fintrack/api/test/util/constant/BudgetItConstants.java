package com.maemresen.fintrack.api.test.util.constant;

import com.maemresen.fintrack.api.test.util.helper.ApiUriHelper;
import com.maemresen.fintrack.api.utils.constants.UriConstant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BudgetItConstants {
    public static final String URI_CREATE = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.CREATE);
    public static final String URI_FIND_BY_ID = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.FIND_BY_ID);
    public static final String URI_FIND_ALL = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.FIND_ALL);
    public static final String URI_ADD_STATEMENT = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.ADD_STATEMENT);
    public static final String URI_REMOVE_STATEMENT = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.REMOVE_STATEMENT);

    @UtilityClass
    public static final class UseCaseEditBudget {
        public static final int BUDGET_INDEX = 0;
    }

    @UtilityClass
    public static final class UseCaseAddStatement {
        public static final int NO_INITIAL_STATEMENTS_BUDGET_INDEX = 1;

        public static final int MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX = 2;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX = 0;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX = 1;
    }

    @UtilityClass
    public static final class UseCaseRemoveStatement {
        public static final int SINGLE_INITIAL_STATEMENT_BUDGET_INDEX = 3;
        public static final int SINGLE_INITIAL_STATEMENT_STATEMENT_INDEX = 0;

        public static final int MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX = 4;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX = 0;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX = 1;
    }

    public static final Double STATEMENT_FOR_ADD_STATEMENT_AMOUNT = 100D;
}
