package com.mracover.telegram_bot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


//Настройки бота
@Configuration
@PropertySource("/bot.properties")
@Getter
public class BotConfig {
    @Value("${bot.name}")
    private String botName; //Имя бота
    @Value("${bot.token}")
    private String botToken; //Токен бота
}
