package com.mracover.telegram_bot.service.handlers;

import com.mracover.telegram_bot.components.StringCommandInfo;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.ReplyMessageService;
import com.mracover.telegram_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class OtherCommandHandler {

    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public OtherCommandHandler(ReplyMessageService replyMessageService,
                               UserService userService) {
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }

    public SendMessage startCommand(String chatId) {
        log.info("/start выполнена");
        return replyMessageService.getReplyMessage(chatId, StringCommandInfo.START_TEXT);
    }

    public SendMessage myDataCommand(Long userId, String chatId) {
        User user = userService.findUserByTelegramId(userId);
        if (user == null) {
            return replyMessageService.getReplyMessage(chatId, "Вы еще не пользовались ботом");
        }
        log.info("/dataCommand выполнена");
        return replyMessageService.getReplyMessage(chatId, user.toString());
    }

    public SendMessage deleteDataCommand(Long userId, String chatId) {
        User user = userService.findUserByTelegramId(userId);
        if (user == null) {
            return replyMessageService.getReplyMessage(chatId, "Вы еще не пользовались ботом");
        }
        userService.deleteUserByTelegramId(userId);
        log.info("/deleteData выполнена");
        return replyMessageService.getReplyMessage(chatId, "Ваши данные удалены");
    }

    public SendMessage helpCommand(String chatId) {
        log.info("/info выполнена");
        return replyMessageService.getReplyMessage(chatId, StringCommandInfo.HELP_TEXT);
    }

}
