package com.maemresen.fintrack.persistence.it;

import com.maemresen.fintrack.commons.spring.test.AbstractBasePostgresIT;
import com.maemresen.fintrack.persistence.FintrackPersistenceITApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@Slf4j
@SpringBootTest(classes = FintrackPersistenceITApplication.class)
@ActiveProfiles("it")
public abstract class AbstractBasePersistenceIT extends AbstractBasePostgresIT {
}
