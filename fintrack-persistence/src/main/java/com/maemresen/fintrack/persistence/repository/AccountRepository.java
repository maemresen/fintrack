package com.maemresen.fintrack.persistence.repository;

import com.maemresen.fintrack.persistence.base.repository.BaseRepository;
import com.maemresen.fintrack.persistence.entity.AccountEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends BaseRepository<AccountEntity, Long> {
}
