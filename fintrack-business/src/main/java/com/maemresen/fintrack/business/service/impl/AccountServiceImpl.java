package com.maemresen.fintrack.business.service.impl;

import com.maemresen.fintrack.business.dto.AccountCreateDto;
import com.maemresen.fintrack.business.service.AccountService;
import com.maemresen.fintrack.persistence.entity.AccountEntity;
import com.maemresen.fintrack.persistence.repository.AccountRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;


    public List<AccountEntity> findAll(){
        return accountRepository.findAll();
    }

    @Override
    public Optional<AccountEntity> findById(@NotNull Long id){
        return accountRepository.findById(id);
    }

    @Override
    public AccountEntity create(@NotNull AccountCreateDto accountCreateDto){
        final var accountEntity = AccountEntity.builder()
            .username(accountCreateDto.getUsername())
            .password(accountCreateDto.getPassword())
            .email(accountCreateDto.getEmail())
            .build();
        return accountRepository.save(accountEntity);
    }
}
