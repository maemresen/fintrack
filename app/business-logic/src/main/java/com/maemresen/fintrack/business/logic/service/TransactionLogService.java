package com.maemresen.fintrack.business.logic.service;

import com.maemresen.fintrack.business.logic.dto.TransactionLogCreateRequestDto;
import com.maemresen.fintrack.business.logic.dto.TransactionLogDto;
import com.maemresen.fintrack.business.logic.mapper.TransactionLogMapper;
import com.maemresen.fintrack.persistence.entity.TransactionLog;
import com.maemresen.fintrack.persistence.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionLogService {

    private final TransactionLogRepository repository;
    private final TransactionLogMapper mapper;

    public List<TransactionLogDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::map)
                .toList();
    }

    public TransactionLogDto create(TransactionLogCreateRequestDto createRequestDto) {
        TransactionLog transactionLog = mapper.map(createRequestDto);
        return mapper.map(repository.save(transactionLog));
    }
}
