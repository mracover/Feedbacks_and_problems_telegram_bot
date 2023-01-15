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

    String HELP_TEXT = """
            Бот для обратной связи и отзывов магазина электроники.
            Список команд и их описание полное описание:
            
            /start - команда стартующая бота. Для начала работы используйте ее, чтобы получить интрукцию по дальнейшим действиям.
            /problem - вы можете прислать свой вопрос по товару или сообщение о проблеме используя эту команду. Следуйте дальнейшим инструкция после использования этой команды.
            /feedback - используйте эту команду чтобы оставить отзыв о купленом товаре.
            /myData - узнайте вашу почту и имя, которую вы оставили, а так же список отзывов и проблем оставленных вами за все время.
            /deleteData - удалить все данные о вас и список ваших обращений за все время.
            /help - полное описание всех команд (вы здесь).
            """;

}
