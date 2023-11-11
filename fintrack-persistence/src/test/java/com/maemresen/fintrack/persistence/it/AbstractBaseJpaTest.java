package com.maemresen.fintrack.persistence.it;

import com.maemresen.fintrack.commons.spring.test.AbstractBasePostgresIT;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public abstract class AbstractBaseJpaTest extends AbstractBasePostgresIT {
}
