package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.botapi.TelegramFacade;
import com.mracover.telegram_bot.components.BotCommands;
import com.mracover.telegram_bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {

    private final BotConfig botConfig;
    private final TelegramFacade telegramFacade;

    public TelegramBot(BotConfig botConfig, TelegramFacade telegramFacade) {
        this.botConfig = botConfig;
        this.telegramFacade = telegramFacade;
        try {
            this.execute(new SetMyCommands(BOT_COMMAND_LIST, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException ex) {
            log.error("Error setting bot`s command list: " + ex.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage replyMessageToUser = telegramFacade.handleUpdate(update);
        try {
            execute(replyMessageToUser);
        } catch (TelegramApiException ex) {
            log.error("Error: " + ex.getMessage());
        }
    }
}
