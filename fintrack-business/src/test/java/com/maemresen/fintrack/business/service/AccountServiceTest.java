package com.maemresen.fintrack.business.service;

import com.maemresen.fintrack.business.service.impl.AccountServiceImpl;
import com.maemresen.fintrack.persistence.entity.AccountEntity;
import com.maemresen.fintrack.persistence.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void findByIdReturnAccount() {
        final String email = "mock@mail";
        final AccountEntity accountEntity = AccountEntity.builder()
            .email(email)
            .build();
        when(accountRepository.findById(any())).thenReturn(Optional.of(accountEntity));

        Optional<AccountEntity> foundAccountEntity = assertDoesNotThrow(() -> accountService.findById(1L));

        assertTrue(foundAccountEntity.isPresent());
        assertEquals(email, foundAccountEntity.get().getEmail());
    }
}
