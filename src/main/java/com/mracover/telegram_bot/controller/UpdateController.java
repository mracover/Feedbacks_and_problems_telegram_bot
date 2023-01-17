package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.components.StringCommandInfo;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.ReplyMessageService;
import com.mracover.telegram_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class UpdateController {

    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public UpdateController(ReplyMessageService replyMessageService,
                            UserService userService) {
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }

    public SendMessage startCommand(String chatId) {
        return replyMessageService.getReplyMessage(chatId, StringCommandInfo.START_TEXT);
    }

    public SendMessage myDataCommand(Long userId, String chatId) {
        User user = userService.findUserByTelegramId(userId);
        return replyMessageService.getReplyMessage(chatId, user.toString());
    }

    public SendMessage deleteDataCommand(Long userId, String chatId) {
        userService.deleteUserByTelegramId(userId);
        return replyMessageService.getReplyMessage(chatId, "Ваши данные удалены");
    }

    public SendMessage helpCommand(String chatId) {
        return replyMessageService.getReplyMessage(chatId, StringCommandInfo.HELP_TEXT);
    }

}
