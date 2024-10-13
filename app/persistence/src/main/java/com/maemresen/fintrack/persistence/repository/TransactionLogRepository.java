package com.maemresen.fintrack.persistence.repository;

import com.maemresen.fintrack.persistence.base.BaseRepository;
import com.maemresen.fintrack.persistence.entity.TransactionLog;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionLogRepository extends BaseRepository<TransactionLog> {

    List<TransactionLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
