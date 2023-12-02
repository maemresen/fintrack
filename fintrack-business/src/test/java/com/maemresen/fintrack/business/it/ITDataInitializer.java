package com.maemresen.fintrack.business.it;

import com.maemresen.fintrack.persistence.entity.AccountEntity;
import com.maemresen.fintrack.persistence.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class ITDataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        final Set<AccountEntity> build = Set.of(
            AccountEntity.builder()
                .id(1L)
                .username("ituser1")
                .password("itpass1")
                .email("ituser@gumail.com")
                .build(),
            AccountEntity.builder()
                .id(2L)
                .username("ituser2")
                .password("itpass2")
                .email("ituser@gumail.com")
                .build()
        );
        accountRepository.saveAll(build);
    }
}
