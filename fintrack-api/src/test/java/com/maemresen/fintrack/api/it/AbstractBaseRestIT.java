package com.maemresen.fintrack.api.it;

import com.maemresen.fintrack.commons.spring.test.AbstractBasePostgresIT;
import com.maemresen.fintrack.commons.spring.test.RestApiIT;
import org.springframework.beans.factory.annotation.Autowired;

@RestApiIT(config = RestApiITConfig.class)
public abstract class AbstractBaseRestIT extends AbstractBasePostgresIT {
    @Autowired
    protected RequestPerformer requestPerformer;
}
