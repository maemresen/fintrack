package com.maemresen.fintrack.core.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@AutoConfiguration
public class PersistenceAutoConfiguration {

    @EnableJpaRepositories(basePackages = "com.maemresen.fintrack.persistence.repository")
    @EntityScan(basePackages = "com.maemresen.fintrack.persistence.entity")
    @PropertySource("classpath:persistence.properties")
    @Configuration
    static class PersistenceConfiguration {

        @Bean
        @ConfigurationProperties(prefix = "spring.datasource")
        public DataSourceProperties dataSourceProperties() {
            return new DataSourceProperties();
        }

        @Bean
        public DataSource dataSource() {
            return dataSourceProperties().initializeDataSourceBuilder().build();
        }
    }
}
