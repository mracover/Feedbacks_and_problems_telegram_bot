package com.mracover.telegram_bot.service.handlers.feedback;

import com.mracover.telegram_bot.botapi.BotState;
import com.mracover.telegram_bot.botapi.InputMessageHandler;
import com.mracover.telegram_bot.cache.UserDataCache;
import com.mracover.telegram_bot.components.StringCommandFeedback;
import com.mracover.telegram_bot.model.Feedback;
import com.mracover.telegram_bot.model.Image;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.FeedbackService;
import com.mracover.telegram_bot.service.ReplyMessageService;
import com.mracover.telegram_bot.service.SavePhotoService;
import com.mracover.telegram_bot.service.UserService;
import jakarta.validation.ValidationException;
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
    private final FeedbackService feedbackService;

    private User user = new User();
    private Feedback feedback = new Feedback();

    public FeedbackHandler(ReplyMessageService replyMessageService,
                           UserDataCache userDataCache,
                           UserService userService,
                           SavePhotoService savePhotoService,
                           FeedbackService feedbackService) {
        this.replyMessageService = replyMessageService;
        this.userDataCache = userDataCache;
        this.userService = userService;
        this.savePhotoService = savePhotoService;
        this.feedbackService = feedbackService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (checkingUser(message.getFrom().getId()) &&
                userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FEEDBACK)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_FEEDBACK_PRODUCT);
        }
        if (!checkingUser(message.getFrom().getId()) &&
                userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FEEDBACK)){
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

        if (botState.equals(BotState.ASK_FEEDBACK_EMAIL) ) {
            try {
                user.setName(userAnswer);
                log.info(userAnswer + "- имя пользователя");
                replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_EMAIL);
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_PRODUCT);
            } catch (ValidationException ex){
                log.error("Пользователь ввел неправильное имя:" + ex.getMessage());
                replyToUser = replyMessageService.getReplyMessage(chatId, "Неправильно введено имя, введите еще раз:");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_EMAIL);
            }
        }

        if (botState.equals(BotState.ASK_FEEDBACK_PRODUCT)) {
            try {
                if (userService.findUserByTelegramId(userId) == null) {
                    user.setEmail(userAnswer);
                }
                replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_PRODUCT);
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_MESSAGE);
            } catch (ValidationException ex) {
                log.error("Неправильный email:" + ex.getMessage());
                replyToUser = replyMessageService.getReplyMessage(chatId, "Неправильно введен email, введите еще раз:");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_PRODUCT);
            }
        }

        if (botState.equals(BotState.ASK_FEEDBACK_MESSAGE)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_MESSAGE);
            feedback.setProduct_id(Integer.parseInt(userAnswer));
            log.info(userAnswer +"- номер продукта");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_IMAGES_FEEDBACK);
        }

        if (botState.equals(BotState.ASK_IMAGES_FEEDBACK)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_IMAGE);
            feedback.setFeedbackMessage(userAnswer);
            log.info(userAnswer +"- сообщение отзыва");
            userDataCache.setUsersCurrentBotState(userId, BotState.FEEDBACK_END);
        }


        if (botState.equals(BotState.FEEDBACK_END)) {
            Image image = savePhotoService.downloadPhotoAndGetEntity(inputMsg);
            if (image != null) {
                log.info("фоторафия скачана и сохранена");
            }
            saveFeedback(user, feedback, image);
            log.info("Все данные успешно сохранены");

            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_END);
            userDataCache.setUsersCurrentBotState(userId, BotState.START);
        }
        return replyToUser;
    }

    private void saveFeedback(User user, Feedback feedback, Image image) {
        User upUser = userService.findUserByTelegramId(user.getTelegramUserId());
        if(image != null) {
            feedback.setImage(image);
        }
        if (upUser == null) {
            user.setFeedback(feedback);
            userService.addUser(user);
        } else {
            feedback.setUser(upUser);
            feedbackService.addFeedback(feedback);
        }
    }

    private boolean checkingUser(long userId) {
        User userByTelegramId = userService.findUserByTelegramId(userId);
        return userByTelegramId != null;
    }

}
