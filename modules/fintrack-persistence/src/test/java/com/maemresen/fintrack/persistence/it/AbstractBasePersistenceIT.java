package com.maemresen.fintrack.persistence.it;

import com.maemresen.fintrack.commons.spring.test.AbstractBasePostgresIT;
import com.maemresen.fintrack.persistence.FintrackPersistenceITApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = FintrackPersistenceITApplication.class)
public abstract class AbstractBasePersistenceIT extends AbstractBasePostgresIT {
}
