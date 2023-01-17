package com.mracover.telegram_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

//формирует готовые ответы
@Service
public class ReplyMessageService {
    public SendMessage getReplyMessage(String chatId, String replyMessage) {
        return new SendMessage(chatId, replyMessage);
    }
}
