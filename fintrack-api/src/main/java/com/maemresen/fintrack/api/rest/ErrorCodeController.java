package com.maemresen.fintrack.api.rest;


import com.maemresen.fintrack.api.dto.ErrorCodeDto;
import com.maemresen.fintrack.api.service.ErrorCodeService;
import com.maemresen.fintrack.api.utils.constants.UriConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(UriConstant.ErrorCode.BASE_URI)
public class ErrorCodeController {

    private final ErrorCodeService errorCodeService;

    @GetMapping(UriConstant.ErrorCode.FIND_ALL_URI)
    public ResponseEntity<List<ErrorCodeDto>> findAll() {
        return ResponseEntity.ok(errorCodeService.findAll());
    }
}
