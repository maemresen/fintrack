package com.maemresen.fintrack.business.it;

import com.maemresen.fintrack.business.dto.AccountCreateDto;
import com.maemresen.fintrack.business.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class AccountServiceIT extends AbstractBaseBusinessIT {

    @Autowired
    private AccountService accountService;

    @Transactional
    @Test
    void test1(){
        accountService.create(AccountCreateDto.builder()
            .username("emre")
            .password("pass")
            .email("emre.sen@yazilim.vip")
            .build());
        System.out.println(accountService.findAll());
    }
}
