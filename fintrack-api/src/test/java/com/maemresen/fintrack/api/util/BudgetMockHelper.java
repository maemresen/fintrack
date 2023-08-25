package com.maemresen.fintrack.api.util;

import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.argThat;

@UtilityClass
public class BudgetMockHelper {
    public static BudgetEntity createMockBudgetEntityWithoutId() {
        return createMockBudgetEntityWithId(null);
    }

    public static BudgetEntity createMockBudgetEntityWithId(Long id) {
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setId(id);
        return budgetEntity;
    }

    public static BudgetEntity createMockBudgetEntityWithIdAndStatements(Long id, StatementEntity... statements) {
        BudgetEntity budgetEntity = createMockBudgetEntityWithId(id);

        if (statements != null) {
            budgetEntity.setStatements(new ArrayList<>(List.of(statements)));
        }
        return budgetEntity;
    }

    public static BudgetDto createMockBudgetDtoWithId(Long id) {
        return createMockBudgetDtoWithIdAndStatements(id);
    }

    public static BudgetDto createMockBudgetDtoWithIdAndStatements(Long id, StatementDto... statements) {
        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setId(id);
        budgetDto.setStatements(SetUtils.hashSet(statements));
        return budgetDto;
    }

    public static BudgetCreateRequestDto createMockBudgetCreateRequestDto(String name) {
        BudgetCreateRequestDto budgetCreateRequestDto = new BudgetCreateRequestDto();
        budgetCreateRequestDto.setName(name);
        return budgetCreateRequestDto;
    }


    public static BudgetEntity getBudgetEntityIdAndStatementsArgumentMatcher(Long id, StatementEntity... statements) {
        return argThat(arg -> {
            if (!arg.getId().equals(id)) {
                return false;
            }

            if (statements == null) {
                return arg.getStatements() == null;
            }

            if (CollectionUtils.size(arg.getStatements()) != statements.length) {
                return false;
            }

            return arg.getStatements().containsAll(Set.of(statements));
        });
    }
}
