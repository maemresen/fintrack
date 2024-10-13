package com.maemresen.fintrack.api.rest;

import com.maemresen.fintrack.business.logic.dto.TransactionLogDto;
import com.maemresen.fintrack.business.logic.service.TransactionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("transaction-log")
public class TransactionLogRest {

    private final TransactionLogService service;

    @GetMapping
    public List<TransactionLogDto> getTransactionLogs() {
        return service.getAll();
    }
}
