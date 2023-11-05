package com.maemresen.fintrack.api.integration.test.base;

import com.maemresen.fintrack.api.integration.test.config.RestApiITConfig;
import com.maemresen.fintrack.api.integration.test.util.RequestPerformer;
import com.maemresen.fintrack.commons.spring.test.base.AbstractBasePostgresIT;
import com.maemresen.fintrack.commons.spring.test.util.RestApiIT;
import org.springframework.beans.factory.annotation.Autowired;

@RestApiIT(config = RestApiITConfig.class)
public abstract class AbstractBaseRestIT extends AbstractBasePostgresIT {
    @Autowired
    protected RequestPerformer requestPerformer;
}
