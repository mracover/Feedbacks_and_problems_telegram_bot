package com.mracover.telegram_bot.botapi.handlers.problem;

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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ProblemsHandler implements InputMessageHandler {

    private final ReplyMessageService replyMessageService;
    private final UserDataCache userDataCache;
    private final UserService userService;
    private final SavePhotoService savePhotoService;

    private final User user = new User();
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
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.PROBLEM)) {
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
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_EMAIL);
            user.setName(userAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_PRODUCT);
        }

        if (botState.equals(BotState.ASK_PROBLEM_PRODUCT)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_PRODUCT);
            user.setEmail(userAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PROBLEM_MESSAGE);
        }

        if (botState.equals(BotState.ASK_PROBLEM_MESSAGE)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_TEXT_MESSAGE);
            problem.setProduct_id(Integer.parseInt(userAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_IMAGES_PROBLEM);
        }

        if (botState.equals(BotState.ASK_IMAGES_PROBLEM)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_IMAGE);
            problem.setProblemMessage(userAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.PROBLEM_END);
        }

        if (botState.equals(BotState.PROBLEM_END)) {
            Image image = savePhotoService.downloadPhotoAndGetEntity(inputMsg);
            saveProblem(user, problem, image);

            replyToUser = replyMessageService.getReplyMessage(chatId, StringCommandProblem.PROBLEM_END);
            userDataCache.setUsersCurrentBotState(userId, BotState.START);
        }

        return replyToUser;
    }

    private void saveProblem(User user, Problem problem, Image image) {
        problem.setImage(image);
        user.setProblem(problem);
        userService.addUser(user);
    }
}
