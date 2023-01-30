package com.mracover.telegram_bot.service.handlers.problem;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.mracover.telegram_bot.botapi.BotState;
import com.mracover.telegram_bot.cache.UserDataCache;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.ReplyMessageService;
import com.mracover.telegram_bot.service.SavePhotoService;
import com.mracover.telegram_bot.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProblemsHandlerTest {

    private UserDataCache userDataCache;
    private UserService userService;
    private ProblemsHandler problemsHandler;

    private ListAppender<ILoggingEvent> appender;
    private final ch.qos.logback.classic.Logger appLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProblemsHandler.class);

    private final User user = new User();

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        ReplyMessageService replyMessageService = Mockito.mock(ReplyMessageService.class);
        this.userService = Mockito.mock(UserService.class);
        this.userDataCache = Mockito.mock(UserDataCache.class);
        SavePhotoService savePhotoService = Mockito.mock(SavePhotoService.class);
        this.problemsHandler = new ProblemsHandler(replyMessageService, userDataCache, userService, savePhotoService);
    }

    @BeforeEach
    public void setUp() {
        appender = new ListAppender<>();
        appender.start();
        appLogger.addAppender(appender);
    }

    @AfterEach
    public void tearDown() {
        appLogger.detachAppender(appender);
    }

    @Test
    void shouldUserFoundHandle() {
        Mockito.doReturn(user).when(userService).findUserByTelegramId(Mockito.anyLong());
        Mockito.doReturn(BotState.PROBLEM).when(userDataCache).getUsersCurrentBotState(Mockito.anyLong());
        Message message = Mockito.mock(Message.class);
        Mockito.doReturn(new org.telegram.telegrambots.meta.api.objects.User(1L, "1", true))
                .when(message).getFrom();
        problemsHandler.handle(message);
        assertEquals("Пользователь найден", appender.list.get(0).getFormattedMessage());
    }

    @Test
    void shouldUserNotFoundHandle() {
        Mockito.doReturn(null).when(userService).findUserByTelegramId(Mockito.anyLong());
        Mockito.doReturn(BotState.PROBLEM).when(userDataCache).getUsersCurrentBotState(Mockito.anyLong());
        Message message = Mockito.mock(Message.class);
        Mockito.doReturn(new org.telegram.telegrambots.meta.api.objects.User(1L, "1", true))
                .when(message).getFrom();
        problemsHandler.handle(message);
        assertEquals("Пользователь не найден", appender.list.get(0).getFormattedMessage());
    }

}