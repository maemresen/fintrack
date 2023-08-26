package com.maemresen.fintrack.api.test.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test-h2"})
public abstract class AbstractBaseServiceTest {
    // This class is used to create a base class for all service tests.
}
