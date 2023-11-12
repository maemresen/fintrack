package com.maemresen.fintrack.persistence;

import com.maemresen.fintrack.persistence.entity.AccountEntity;
import com.maemresen.fintrack.persistence.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class FintrackPersistenceITApplication implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("-------------------------------------------------------------- DATA LOAD --------------------------------------------------------------");
        log.info("fancy data loading stuff....");
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail("Email");
        accountRepository.save(accountEntity);
        log.info("-------------------------------------------------------------- #DATA LOAD --------------------------------------------------------------");
    }
}
