package com.mracover.telegram_bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;

    public TelegramBot(
            TelegramBotsApi botsApi,
            @Value("${bot.token}") String botToken,
            @Value("${bot.name}") String botName) throws TelegramApiException {
            this.botName = botName;
            this.botToken = botToken;
            botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        System.out.println(message.getText());
    }
}
