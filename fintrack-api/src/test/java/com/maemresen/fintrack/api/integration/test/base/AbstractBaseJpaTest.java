package com.maemresen.fintrack.api.integration.test.base;

import com.maemresen.fintrack.commons.spring.test.base.AbstractBasePostgresIT;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public abstract class AbstractBaseJpaTest extends AbstractBasePostgresIT {
}
