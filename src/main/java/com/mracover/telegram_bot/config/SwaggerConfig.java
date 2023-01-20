package com.mracover.telegram_bot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("API документация для Telegram bot")
                        .version("1.0.0")
                        .description("Доступ к базе данных бота")
                        .contact(
                                new Contact()
                                        .name("mracover")
                                        .email("mracover@gmail.com"))
        );
    }
}
