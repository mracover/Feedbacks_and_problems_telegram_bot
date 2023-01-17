package com.mracover.telegram_bot.components;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List <BotCommand> BOT_COMMAND_LIST = List.of(
            new BotCommand("/start", "Начать работу"),
            new BotCommand("/problem", "Оставить свой вопрос или сообщение о проблеме"),
            new BotCommand("/feedback", "Оставить отзыв"),
            new BotCommand("/mydata", "Ваши запросы и данные"),
            new BotCommand("/deletedata", "Удалить ваши запросы и данные"),
            new BotCommand("/help", "Полное описание команд")
    );
}
