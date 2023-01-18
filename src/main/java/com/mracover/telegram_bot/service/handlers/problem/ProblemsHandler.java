package com.mracover.telegram_bot.service.handlers.problem;

import com.mracover.telegram_bot.botapi.BotState;
import com.mracover.telegram_bot.botapi.InputMessageHandler;
import com.mracover.telegram_bot.cache.UserDataCache;
import com.mracover.telegram_bot.components.StringCommandProblem;
import com.mracover.telegram_bot.model.Image;
import com.mracover.telegram_bot.model.Problem;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.ProblemService;
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
public class ProblemsHandler implements InputMessageHandler {

    private final ReplyMessageService replyMessageService;
    private final UserDataCache userDataCache;
    private final UserService userService;
    private final SavePhotoService savePhotoService;
    private final ProblemService problemService;
    private User user = new User();
    private final Problem problem = new Problem();

    public ProblemsHandler(ReplyMessageService replyMessageService,
                           UserDataCache userDataCache,
                           UserService userService,
                           SavePhotoService savePhotoService,
                           ProblemService problemService) {
        this.replyMessageService = replyMessageService;
        this.userDataCache = userDataCache;
        this.userService = userService;
        this.savePhotoService = savePhotoService;
        this.problemService = problemService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (checkingUser(message.getFrom().getId()) &&
                userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.PROBLEM)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_PROBLEM_PRODUCT);
        }
        if (!checkingUser(message.getFrom().getId()) &&
                userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.PROBLEM)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_PROBLEM_NAME);
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

        user.setTelegramUserId(userId);

        if (botState.equals(BotState.ASK_PROBLEM_NAME)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_NAME);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_EMAIL);
        }

        if (botState.equals(BotState.ASK_PROBLEM_EMAIL)) {
            try {
                user.setName(userAnswer);
                log.info(userAnswer + "-  имя");
                replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_EMAIL);
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_PRODUCT);
            } catch (ValidationException ex){
                log.error("Пользователь ввел неправильное имя:" + ex.getMessage());
                replyToUser = replyMessageService.getReplyMessage(chatId, "Неправильно введено имя, введите еще раз:");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_EMAIL);
            }
        }

        if (botState.equals(BotState.ASK_PROBLEM_PRODUCT)) {
            try {
                if (userService.findUserByTelegramId(userId) == null) {
                    user.setEmail(userAnswer);
                }
                log.info(userAnswer + "- email");
                replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_PRODUCT);
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_MESSAGE);
            } catch (ValidationException ex) {
                log.error("Неправильный email:" + ex.getMessage());
                replyToUser = replyMessageService.getReplyMessage(chatId, "Неправильно введен email, введите еще раз:");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_PRODUCT);
            }
        }

        if (botState.equals(BotState.ASK_PROBLEM_MESSAGE)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_MESSAGE);
            problem.setProduct_id(Integer.parseInt(userAnswer));
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
            Image image = savePhotoService.downloadPhotoAndGetEntity(inputMsg);
            if (image != null) {
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
        User upUser = userService.findUserByTelegramId(user.getTelegramUserId());
        if(image != null) {
            problem.setImage(image);
        }
        if (upUser == null) {
            user.setProblem(problem);
            userService.addUser(user);
        } else {
            problem.setUser(upUser);
            problemService.addProblem(problem);
        }
    }

    private boolean checkingUser(long userId) {
        user = userService.findUserByTelegramId(userId);
        return user != null;
    }
}
