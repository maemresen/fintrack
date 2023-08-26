package com.maemresen.fintrack.api.service.impl;

import com.maemresen.fintrack.api.dto.ErrorCodeDto;
import com.maemresen.fintrack.api.service.ErrorCodeService;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ErrorCodeServiceImpl implements ErrorCodeService {
    @Override
    public List<ErrorCodeDto> findAll() {
        return Arrays.stream(ExceptionType.values()).map(exceptionType -> ErrorCodeDto.builder()
            .code(exceptionType.getCode())
            .name(exceptionType.name())
            .build()).toList();
    }
}
