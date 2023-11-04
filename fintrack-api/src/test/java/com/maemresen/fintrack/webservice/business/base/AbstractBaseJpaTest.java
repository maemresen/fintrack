package com.maemresen.fintrack.webservice.business.base;

import com.maemresen.fintrack.webservice.business.MockSpringBootApplication;
import com.maemresen.fintrack.commons.spring.test.base.AbstractBasePostgresIT;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {MockSpringBootApplication.class})
public abstract class AbstractBaseJpaTest extends AbstractBasePostgresIT {
}
