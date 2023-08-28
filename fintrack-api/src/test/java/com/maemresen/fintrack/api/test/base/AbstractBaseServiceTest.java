package com.maemresen.fintrack.api.test.base;

import com.maemresen.fintrack.api.test.util.constant.UseInMemoryDB;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@UseInMemoryDB
public abstract class AbstractBaseServiceTest {
    // This class is used to create a base class for all service tests.
}
