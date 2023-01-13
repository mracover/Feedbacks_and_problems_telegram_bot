package com.mracover.telegram_bot.service;

import com.mracover.telegram_bot.model.Message;

import java.util.List;

public interface MessageService {
    Message addMessage(Message message);
    List<Message> getAllMessage();
    Message findMessageById(String id);
    Message updateMessage(Message message);
    void deleteMessageById(String id);
}
