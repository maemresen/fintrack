package com.maemresen.fintrack.webservice.api.base;

import com.maemresen.fintrack.webservice.api.FintrackApplication;
import com.maemresen.fintrack.webservice.api.config.RestApiITConfig;
import com.maemresen.fintrack.webservice.api.util.RequestPerformer;
import com.maemresen.fintrack.commons.spring.test.base.AbstractBasePostgresIT;
import com.maemresen.fintrack.commons.spring.test.util.RestApiIT;
import org.springframework.beans.factory.annotation.Autowired;

@RestApiIT(context = FintrackApplication.class, config = RestApiITConfig.class)
public abstract class AbstractBaseRestIT extends AbstractBasePostgresIT {
    @Autowired
    protected RequestPerformer requestPerformer;
}
