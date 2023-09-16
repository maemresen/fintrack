package com.maemresen.fintrack.webservice.api.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProps {
    private boolean exceptionsStackTraceEnabled;
}
