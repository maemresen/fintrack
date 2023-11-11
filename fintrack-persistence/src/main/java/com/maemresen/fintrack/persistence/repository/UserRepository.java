package com.maemresen.fintrack.persistence.repository;

import com.maemresen.fintrack.persistence.base.repository.BaseRepository;
import com.maemresen.fintrack.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
}
