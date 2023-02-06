package com.matheusjmoura.currencyconversion.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openApiConfig() {
        return new OpenAPI().info(new Info()
            .title("Currency Conversion REST API")
            .version("1.0.0")
            .description("An App that convert two currencies with updated tax rate.")
            .contact(new Contact()
                .name("Matheus José de Moura")
                .url("https://www.linkedin.com/in/matheusjmoura/")
                .email("matheusjosemoura@hotmail.com")));
    }

}
