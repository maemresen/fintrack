package com.maemresen.fintrack.api.test.util.constant;

import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.test.util.helper.ApiUriHelper;
import com.maemresen.fintrack.api.utils.constants.UriConstant;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class BudgetItConstants {
    public static final String URI_CREATE = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.CREATE_URI);
    public static final String URI_FIND_BY_ID = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.FIND_BY_ID_URI);
    public static final String URI_FIND_ALL = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.FIND_ALL_URI);
    public static final String URI_ADD_STATEMENT = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.ADD_STATEMENT_URI);
    public static final String URI_REMOVE_STATEMENT = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.REMOVE_STATEMENT_URI);
    public static final String URI_MONTHLY_REPORT_FOR_YEAR = ApiUriHelper.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.MONTHLY_REPORT_FOR_YEAR_URI);

    @UtilityClass
    public static final class EditBudget {
        public static final int BUDGET_INDEX = 0;
    }

    @UtilityClass
    public static final class AddStatement {
        public static final int NO_INITIAL_STATEMENTS_BUDGET_INDEX = 1;

        public static final int MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX = 2;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX = 0;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX = 1;
    }

    @UtilityClass
    public static final class RemoveStatement {
        public static final int SINGLE_INITIAL_STATEMENT_BUDGET_INDEX = 3;
        public static final int SINGLE_INITIAL_STATEMENT_STATEMENT_INDEX = 0;

        public static final int MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX = 4;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX = 0;
        public static final int MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX = 1;
    }

    @UtilityClass
    public static final class MonthlyReportForYear {
        public static final int BUDGET_INDEX = 5;
        public static final int YEAR = 2023;

        public record Statement(int index, LocalDateTime date, double amount, StatementType type) {
        }

        // August Statements
        public static final Statement STATEMENT_AUG_INCOME = new Statement(0, LocalDateTime.of(YEAR, 8, 6, 0, 22, 11), 500.0, StatementType.INCOME);
        public static final Statement STATEMENT_AUG_EXPENSE_1 = new Statement(1, LocalDateTime.of(YEAR, 8, 7, 0, 22, 11), 600.0, StatementType.EXPENSE);
        public static final Statement STATEMENT_AUG_EXPENSE_2 = new Statement(2, LocalDateTime.of(YEAR, 8, 8, 0, 22, 11), 200.0, StatementType.EXPENSE);

        // September Statements
        public static final Statement STATEMENT_SEP_INCOME_1 = new Statement(3, LocalDateTime.of(YEAR, 9, 6, 0, 22, 11), 700.0, StatementType.INCOME);
        public static final Statement STATEMENT_SEP_INCOME_2 = new Statement(4, LocalDateTime.of(YEAR, 9, 7, 0, 22, 11), 400.0, StatementType.INCOME);
        public static final Statement STATEMENT_SEP_EXPENSE_1 = new Statement(5, LocalDateTime.of(YEAR, 9, 8, 0, 22, 11), 300.0, StatementType.EXPENSE);
        public static final Statement STATEMENT_SEP_EXPENSE_2 = new Statement(6, LocalDateTime.of(YEAR, 9, 9, 0, 22, 11), 200.0, StatementType.EXPENSE);
        public static final Statement STATEMENT_SEP_EXPENSE_3 = new Statement(7, LocalDateTime.of(YEAR, 9, 10, 0, 22, 11), 100.0, StatementType.EXPENSE);

        public static final List<Statement> STATEMENTS = List.of(
            STATEMENT_AUG_INCOME,
            STATEMENT_AUG_EXPENSE_1,
            STATEMENT_AUG_EXPENSE_2,
            STATEMENT_SEP_INCOME_1,
            STATEMENT_SEP_INCOME_2,
            STATEMENT_SEP_EXPENSE_1,
            STATEMENT_SEP_EXPENSE_2,
            STATEMENT_SEP_EXPENSE_3
        );
    }

    public static final Double STATEMENT_FOR_ADD_STATEMENT_AMOUNT = 100D;
}
