package com.mracover.telegram_bot.botapi;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

//Интерфейс описывает обработчики feedback и problem
public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotState getHandlerName();
}
