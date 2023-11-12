package com.maemresen.fintrack.persistence.it;

import com.maemresen.fintrack.persistence.entity.AccountEntity;
import com.maemresen.fintrack.persistence.repository.AccountRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloPersistenceIT extends AbstractBasePersistenceIT {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void persistenceTest1(){
        final List<AccountEntity> accountEntities = assertDoesNotThrow(() -> accountRepository.findAll());
        assertTrue(CollectionUtils.isNotEmpty(accountEntities));
    }
}
