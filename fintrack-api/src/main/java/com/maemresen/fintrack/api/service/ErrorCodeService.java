package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.ErrorCodeDto;

import java.util.List;

public interface ErrorCodeService {
    List<ErrorCodeDto> findAll();
}
