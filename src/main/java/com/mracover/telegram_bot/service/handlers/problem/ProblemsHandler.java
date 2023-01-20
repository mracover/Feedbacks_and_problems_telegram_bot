package com.mracover.telegram_bot.service.handlers.problem;

import com.mracover.telegram_bot.botapi.BotState;
import com.mracover.telegram_bot.botapi.InputMessageHandler;
import com.mracover.telegram_bot.cache.UserDataCache;
import com.mracover.telegram_bot.components.StringCommandProblem;
import com.mracover.telegram_bot.model.Image;
import com.mracover.telegram_bot.model.Problem;
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
public class ProblemsHandler implements InputMessageHandler {

    private final ReplyMessageService replyMessageService;
    private final UserDataCache userDataCache;
    private final UserService userService;
    private final SavePhotoService savePhotoService;
    private User user;
    private final Problem problem = new Problem();

    public ProblemsHandler(ReplyMessageService replyMessageService,
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
        //Проверка пользователя в бд, пропускает этап имя и почты
        if (checkingUser(userId) &&
                userDataCache.getUsersCurrentBotState(userId).equals(BotState.PROBLEM)) {
            user = userService.findUserByTelegramId(userId);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_PRODUCT);
            log.info("Пользователь найден");
        }
        //Спрашивает имя, если пользователь не найден
        if (!checkingUser(userId) &&
                userDataCache.getUsersCurrentBotState(userId).equals(BotState.PROBLEM)) {
            user = new User();
            user.setTelegramUserId(userId);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.PROBLEM;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String userAnswer = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        String chatId = String.valueOf(inputMsg.getChatId());
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_PROBLEM_NAME)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_NAME);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_EMAIL);
        }

        if (botState.equals(BotState.ASK_PROBLEM_EMAIL)) {
            if (userAnswer.length() > 20) {
                log.error("Пользователь ввел неправильное имя");
                replyToUser = replyMessageService.getReplyMessage(chatId, "Неправильно введено имя, введите еще раз:");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_EMAIL);
            } else {
                user.setName(userAnswer);
                log.info(userAnswer + "- имя пользователя");
                replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_EMAIL);
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_PRODUCT);
            }
        }

        if (botState.equals(BotState.ASK_PROBLEM_PRODUCT)) {
            if (user.getEmail() == null) {
                user.setEmail(userAnswer);
            }
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_PRODUCT);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_MESSAGE);
        }

        if (botState.equals(BotState.ASK_PROBLEM_MESSAGE)) {
            try {
                problem.setProduct_id(Integer.parseInt(userAnswer));
            } catch (ClassCastException ex) {
                log.error(ex.getMessage());
                replyToUser = replyMessageService.getReplyMessage(chatId, "Неправильное id, введите еще раз:");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_MESSAGE);
            }
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_MESSAGE);
            log.info(userAnswer + "- номер продукта");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_IMAGES_PROBLEM);
        }

        if (botState.equals(BotState.ASK_IMAGES_PROBLEM)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_IMAGE);
            problem.setProblemMessage(userAnswer);
            log.info(userAnswer + "- сообщение о проблеме");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROBLEM_END);
        }

        if (botState.equals(BotState.PROBLEM_END)) {
            Image image = null;
            if (inputMsg.hasPhoto()) {
                image = savePhotoService.downloadPhotoAndGetEntity(inputMsg);
                log.info("фоторафия скачана и сохранена");
            }
            saveProblem(user, problem, image);
            log.info("Все данные успешно сохранены");

            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_END);
            userDataCache.setUsersCurrentBotState(userId, BotState.START);
        }

        return replyToUser;
    }

    private void saveProblem(User user, Problem problem, Image image) {
        Problem problem1 = new Problem();
        problem1.setProblemMessage(problem.getProblemMessage());
        problem1.setProduct_id(problem.getProduct_id());
        if(image != null) {
            image.setFeedback(null);
            problem1.setImage(image);
            log.info("фотография присвоенна");
        }
        user.setProblem(problem1);
        userService.addUser(user);
    }

    private boolean checkingUser(long userId) {
        User userByTelegramId = userService.findUserByTelegramId(userId);
        return userByTelegramId != null;
    }
}
