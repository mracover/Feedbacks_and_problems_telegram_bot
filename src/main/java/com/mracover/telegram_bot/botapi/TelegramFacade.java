package com.mracover.telegram_bot.botapi;


import com.mracover.telegram_bot.cache.UserDataCache;
import com.mracover.telegram_bot.controller.UpdateController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

//Обрабатывает входящие сообщение от пользователя и устанавливает состояние боту

@Component
@Slf4j
public class TelegramFacade {

    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private UpdateController updateController;

    public TelegramFacade(BotStateContext botStateContext,
                          UserDataCache userDataCache,
                          UpdateController updateController) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.updateController = updateController;
    }

    //Отправка на обработку
    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        log.info("New message from User:{}, chatId: {},  with text: {}",
                message.getFrom().getUserName(), message.getChatId(), message.getText());
        replyMessage = handleInputMessage(message);
        return replyMessage;
    }

    //Обработка
    private SendMessage handleInputMessage(Message message) {
        long userId = message.getFrom().getId();
        BotState botState = null;
        SendMessage replyMessage = null;
        if (message.isCommand()) {
            String chatId = String.valueOf(message.getChatId());
            String inputMsg = message.getText();
            switch (inputMsg) {
                case "/start":
                    replyMessage = updateController.startCommand(chatId);
                    break;
                case "/problem":
                    botState = BotState.PROBLEM;
                    break;
                case "/feedback":
                    botState = BotState.FEEDBACK;
                    break;
                case "/mydata":
                    replyMessage = updateController.myDataCommand(userId, chatId);
                    break;
                case "/deletedata":
                    replyMessage = updateController.deleteDataCommand(userId, chatId);
                    break;
                case "/help":
                    replyMessage = updateController.helpCommand(chatId);
                    break;
                default:
                    break;
            }
        } else {
            botState = userDataCache.getUsersCurrentBotState(userId);
        }

        if (replyMessage == null) {
            userDataCache.setUsersCurrentBotState(userId, botState);
            replyMessage = botStateContext.processInputMessage(botState, message);
            return replyMessage;
        }

        return replyMessage;
    }
}
