package com.maemresen.fintrack.business.service;

import com.maemresen.fintrack.business.dto.AccountCreateDto;
import com.maemresen.fintrack.persistence.entity.AccountEntity;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountEntity> findAll();

    Optional<AccountEntity> findById(@NotNull Long id);

    AccountEntity create(@NotNull AccountCreateDto accountCreateDto);
}
