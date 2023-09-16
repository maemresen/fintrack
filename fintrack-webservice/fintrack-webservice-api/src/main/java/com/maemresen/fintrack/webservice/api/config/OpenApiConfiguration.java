package com.maemresen.fintrack.webservice.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(info());
    }

    private Info info() {
        return new Info()
            .title("My API")
            .version("1.0.0")
            .description("My custom API for demo")
            .contact(new Contact().name("John Doe").email("maemresen@yazilim.vip"));
    }
}
