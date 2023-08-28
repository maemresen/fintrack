package com.maemresen.fintrack.api.test.util.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class SpringBootPostgreSQLContainer extends PostgreSQLContainer<SpringBootPostgreSQLContainer> {

    public SpringBootPostgreSQLContainer(String dockerImageName) {
        super(dockerImageName);
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", this.getJdbcUrl());
        System.setProperty("spring.datasource.username", this.getUsername());
        System.setProperty("spring.datasource.password", this.getPassword());
        System.setProperty("spring.jpa.hibernate.ddl-auto", "create");
    }
}
