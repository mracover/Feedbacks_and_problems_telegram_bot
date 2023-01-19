package com.mracover.telegram_bot.service.handlers.feedback;

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
    private User user;
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
        long userId = message.getFrom().getId();
        //Если пользователь найден, то пропускает этап имя и почты
        if (checkingUser(userId) &&
                userDataCache.getUsersCurrentBotState(userId).equals(BotState.FEEDBACK)) {
            user = userService.findUserByTelegramId(userId);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_PRODUCT);
            log.info("Пользователь найден");
        }
        //Если пользователь не найден, спрашивает имя
        if (!checkingUser(userId) &&
                userDataCache.getUsersCurrentBotState(userId).equals(BotState.FEEDBACK)){
            user = new User();
            user.setTelegramUserId(userId);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_NAME);
        }
        return processUsersInput(message);
    }
    @Override
    public BotState getHandlerName() {
        return BotState.FEEDBACK;
    }

    //Обработка в зависимости от состояния бота
    private SendMessage processUsersInput(Message inputMsg) {
        String userAnswer = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        String chatId = String.valueOf(inputMsg.getChatId());
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_FEEDBACK_NAME)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_NAME);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_EMAIL);
        }

        if (botState.equals(BotState.ASK_FEEDBACK_EMAIL) ) {
            if (userAnswer.length() > 20) {
                log.error("Пользователь ввел неправильное имя");
                replyToUser = replyMessageService.getReplyMessage(chatId, "Неправильно введено имя, введите еще раз:");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_EMAIL);
            } else {
                user.setName(userAnswer);
                log.info(userAnswer + "- имя пользователя");
                replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_EMAIL);
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_PRODUCT);
            }
        }

        if (botState.equals(BotState.ASK_FEEDBACK_PRODUCT)) {
            if (user.getEmail() == null) {
                user.setEmail(userAnswer);
            }
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandFeedback.FEEDBACK_TEXT_PRODUCT);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FEEDBACK_MESSAGE);
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
            Image image = null;
            if (inputMsg.hasPhoto()) {
                image = savePhotoService.downloadPhotoAndGetEntity(inputMsg);
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
        Feedback feedback1 = new Feedback();
        feedback1.setFeedbackMessage(feedback.getFeedbackMessage());
        feedback1.setProduct_id(feedback.getProduct_id());
        if(image != null) {
            image.setProblem(null);
            feedback1.setImage(image);
            log.info("фотография присвоенна");
        }
        user.setFeedback(feedback1);
        userService.addUser(user);

    }

    private boolean checkingUser(long userId) {
        User userByTelegramId = userService.findUserByTelegramId(userId);
        return userByTelegramId != null;
    }

}
