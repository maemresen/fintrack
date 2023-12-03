package com.maemresen.fintrack.business.it;

import com.maemresen.fintrack.business.it.data.AccountDataLoader;
import com.maemresen.fintrack.business.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(AccountDataLoader.class)
class AccountServiceIT extends AbstractBaseBusinessIT {

    @Autowired
    private AccountService accountService;

    @Test
    void should(){
        System.out.println(accountService.findAll());
    }
}
