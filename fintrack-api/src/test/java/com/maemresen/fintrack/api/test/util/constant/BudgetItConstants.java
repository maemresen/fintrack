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

    public static final int BUDGET_FOR_EDIT_TEST = 0;

    public static final int BUDGET_FOR_STATEMENT_ADDITION_NO_INITIAL_STATEMENTS = 1;

    public static final int BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS = 2;
    public static final int BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1 = 0;
    public static final int BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2 = 1;

    public static final int BUDGET_FOR_SINGLE_STATEMENT_REMOVE = 3;
    public static final int BUDGET_FOR_SINGLE_STATEMENT_REMOVE_STATEMENT_1 = 0;

    public static final int BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE = 4;
    public static final int BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_STATEMENT_1 = 0;
    public static final int BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_STATEMENT_2 = 1;

    public static final Double STATEMENT_FOR_ADD_STATEMENT_AMOUNT = 100D;
}
