package com.maemresen.fintrack.business.it;

import com.maemresen.fintrack.commons.spring.test.AbstractBasePostgresIT;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FintrackBusinessITApplication.class)
public abstract class AbstractBaseBusinessIT extends AbstractBasePostgresIT {
}
