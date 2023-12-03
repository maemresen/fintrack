package com.maemresen.fintrack.business.it.data;

import com.maemresen.fintrack.persistence.entity.AccountEntity;
import com.maemresen.fintrack.persistence.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class AccountDataLoader implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.trace("Initializing IT Data.");
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
        log.trace("IT Data initialized.");
    }
}
