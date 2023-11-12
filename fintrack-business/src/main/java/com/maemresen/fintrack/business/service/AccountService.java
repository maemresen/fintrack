package com.maemresen.fintrack.business.service;

import com.maemresen.fintrack.persistence.entity.AccountEntity;
import com.maemresen.fintrack.persistence.repository.AccountRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public Optional<AccountEntity> findById(@NotNull Long id){
        return accountRepository.findById(id);
    }
}
