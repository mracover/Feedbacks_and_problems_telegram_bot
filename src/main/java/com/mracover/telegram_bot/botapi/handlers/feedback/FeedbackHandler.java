package com.mracover.telegram_bot.botapi.handlers.feedback;

import com.mracover.telegram_bot.botapi.BotState;
import com.mracover.telegram_bot.botapi.InputMessageHandler;
import com.mracover.telegram_bot.cache.UserDataCache;
import com.mracover.telegram_bot.components.StringCommandFeedback;
import com.mracover.telegram_bot.model.Feedback;
import com.mracover.telegram_bot.model.Image;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.ReplyMessageService;
import com.mracover.telegram_bot.service.SavePhotoService;
import com.mracover.telegram_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class FeedbackHandler implements InputMessageHandler {

    private final ReplyMessageService replyMessageService;
    private final UserDataCache userDataCache;
    private final UserService userService;
    private final SavePhotoService savePhotoService;
    private final User user = new User();
    private final Feedback feedback = new Feedback();

    public FeedbackHandler(ReplyMessageService replyMessageService,
                           UserDataCache userDataCache,
                           UserService userService,
                           SavePhotoService savePhotoService) {
        this.replyMessageService = replyMessageService;
        this.userDataCache = userDataCache;
        this.userService = userService;
        this.savePhotoService = savePhotoService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FEEDBACK)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_FEEDBACK_NAME);
        }
        return processUsersInput(message);
    }
    @Override
    public BotState getHandlerName() {
        return BotState.FEEDBACK;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String userAnswer = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        String chatId = String.valueOf(inputMsg.getChatId());
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        user.setTelegramUserId(userId);

        if (botState.equals(BotState.ASK_FEEDBACK_NAME)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_NAME);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_EMAIL);
        }

        if (botState.equals(BotState.ASK_FEEDBACK_EMAIL)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_EMAIL);
            user.setName(userAnswer);
            log.info(user.toString());
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_PRODUCT);
        }

        if (botState.equals(BotState.ASK_FEEDBACK_PRODUCT)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_PRODUCT);
            user.setEmail(userAnswer);
            log.info(user.toString());
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_MESSAGE);
        }

        if (botState.equals(BotState.ASK_FEEDBACK_MESSAGE)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_MESSAGE);
            feedback.setProduct_id(Integer.parseInt(userAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_IMAGES_FEEDBACK);
        }

        if (botState.equals(BotState.ASK_IMAGES_FEEDBACK)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_IMAGE);
            feedback.setFeedbackMessage(userAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.FEEDBACK_END);
        }


        if (botState.equals(BotState.FEEDBACK_END)) {
            Image image = savePhotoService.downloadPhotoAndGetEntity(inputMsg);
            saveFeedback(user, feedback, image);

            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_END);
            userDataCache.setUsersCurrentBotState(userId, BotState.START);
        }
        return replyToUser;
    }

    private void saveFeedback(User user, Feedback feedback, Image image) {
        feedback.setImage(image);
        user.setFeedback(feedback);
        userService.addUser(user);
    }
}
