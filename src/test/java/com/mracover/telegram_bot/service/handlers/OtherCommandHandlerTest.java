package com.mracover.telegram_bot.service.handlers;

import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.ReplyMessageService;
import com.mracover.telegram_bot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OtherCommandHandlerTest {

    private OtherCommandHandler otherCommandHandler;
    private ReplyMessageService replyMessageService;
    private UserService userService;
    private SendMessage sendMessage = new SendMessage();

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        this.replyMessageService = Mockito.mock(ReplyMessageService.class);
        this.userService = Mockito.mock(UserService.class);
        this.otherCommandHandler = new OtherCommandHandler(replyMessageService, userService);
    }

    @Test
    void shouldStartCommand() {
        Mockito.doReturn(sendMessage).when(replyMessageService).getReplyMessage(Mockito.anyString(), Mockito.anyString());
        var answer = otherCommandHandler.startCommand("1");
        assertEquals(answer, sendMessage);
    }

    @Test
    void shouldMyDataCommand() {
        Mockito.doReturn(new User()).when(userService).findUserByTelegramId(Mockito.anyLong());
        Mockito.doReturn(sendMessage).when(replyMessageService).getReplyMessage(Mockito.anyString(), Mockito.anyString());
        var answer = otherCommandHandler.myDataCommand(1L, "1");
        assertEquals(answer, sendMessage);
    }

    @Test
    void shouldNullMessageWhenMyDataCommand() {
        Mockito.doReturn(null).when(userService).findUserByTelegramId(Mockito.anyLong());
        Mockito.doReturn(new SendMessage("1", "Вы еще не пользовались ботом")).when(replyMessageService)
                .getReplyMessage(Mockito.anyString(), Mockito.anyString());
        var answer = otherCommandHandler.myDataCommand(1L, "1");
        assertEquals("Вы еще не пользовались ботом", answer.getText());
    }

    @Test
    void shouldDeleteDataCommand() {
        Mockito.doReturn(new User()).when(userService).findUserByTelegramId(Mockito.anyLong());
        Mockito.doNothing().when(userService).deleteUserByTelegramId(Mockito.anyLong());
        Mockito.doReturn(sendMessage).when(replyMessageService).getReplyMessage(Mockito.anyString(), Mockito.anyString());
        var answer = otherCommandHandler.deleteDataCommand(1L, "1");
        assertEquals(sendMessage, answer);
    }

    @Test
    void shouldNullMessageWhenDeleteDataCommand() {
        Mockito.doReturn(null).when(userService).findUserByTelegramId(Mockito.anyLong());
        Mockito.doReturn(new SendMessage("1", "Вы еще не пользовались ботом")).when(replyMessageService)
                .getReplyMessage(Mockito.anyString(), Mockito.anyString());
        var answer = otherCommandHandler.deleteDataCommand(1L, "1");
        assertEquals("Вы еще не пользовались ботом", answer.getText());
    }

    @Test
    void shouldHelpCommand() {
        Mockito.doReturn(sendMessage).when(replyMessageService).getReplyMessage(Mockito.anyString(), Mockito.anyString());
        var answer = otherCommandHandler.helpCommand("1");
        assertEquals(answer, sendMessage);
    }
}