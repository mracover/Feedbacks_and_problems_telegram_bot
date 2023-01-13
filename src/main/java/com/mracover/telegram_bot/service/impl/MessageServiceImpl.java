package com.mracover.telegram_bot.service.impl;

import com.mracover.telegram_bot.exception.DatabaseException;
import com.mracover.telegram_bot.exception.messageException.NoSuchMessageException;
import com.mracover.telegram_bot.exception.userException.NoSuchUserException;
import com.mracover.telegram_bot.model.Message;
import com.mracover.telegram_bot.repository.MessageRepository;
import com.mracover.telegram_bot.service.MessageService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Override
    @Transactional
    public Message addMessage(Message message) {
        try {
            return messageRepository.save(message);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllMessage() {
        List<Message> messages;
        try {
            messages = messageRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (messages.isEmpty()) {
            throw new NoSuchMessageException("Ваши сообщения не найдены");
        }
        return messageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageById(String id) {
        Optional<Message> message;
        try {
            message = messageRepository.findById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (message.isEmpty()) {
            throw new NoSuchMessageException("Сообщение не найдено");
        }
        return message.get();
    }

    @Override
    @Transactional
    public Message updateMessage(Message message) {
        Optional<Message> updateMessage;
        try {
            updateMessage = Optional.of(messageRepository.save(message));
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (updateMessage.isEmpty()) {
            throw new NoSuchMessageException("Обновляемое сообщение не найдено");
        }
        return updateMessage.get();
    }

    @Override
    @Transactional
    public void deleteMessageById(String id) {
        Optional<Message> message;
        try {
            message = messageRepository.findById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (message.isEmpty()) {
            throw new NoSuchUserException("Удаляемое сообщение не найдено");
        }
        try {
            messageRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
    }
}
