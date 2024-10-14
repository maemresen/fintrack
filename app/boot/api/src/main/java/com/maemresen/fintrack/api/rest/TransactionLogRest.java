package com.maemresen.fintrack.api.rest;

import com.maemresen.fintrack.business.logic.dto.TransactionLogCreateRequestDto;
import com.maemresen.fintrack.business.logic.dto.TransactionLogDto;
import com.maemresen.fintrack.business.logic.service.TransactionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("transaction-log")
public class TransactionLogRest {

    private final TransactionLogService service;

    @PostMapping
    public TransactionLogDto create(@RequestBody TransactionLogCreateRequestDto createRequestDto) {
        return service.create(createRequestDto);
    }

    @GetMapping
    public List<TransactionLogDto> getTransactionLogs() {
        return service.getAll();
    }
}
