package com.mracover.telegram_bot.service.impl;

import com.mracover.telegram_bot.exception.DatabaseException;
import com.mracover.telegram_bot.exception.messageException.NoSuchMessageException;
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
    public Message addMessage(Message message) throws DatabaseException {
        try {
            return messageRepository.save(message);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllMessage() throws DatabaseException, NoSuchMessageException{
        List<Message> messages;
        try {
            messages = messageRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
        if (messages.isEmpty()) {
            throw new NoSuchMessageException("Ваши сообщения не найдены");
        }
        return messages;
    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageById(String id) throws DatabaseException, NoSuchMessageException{
        try {
            return messageRepository.findById(id).orElseThrow(() ->
                    new NoSuchMessageException("Сообщение не найдено"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public Message updateMessage(Message message) throws DatabaseException, NoSuchMessageException{
        try {
            return Optional.of(messageRepository.save(message)).orElseThrow(() ->
                    new NoSuchMessageException("Обновляемое сообщение не найдено"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public void deleteMessageById(String id) throws DatabaseException, NoSuchMessageException{
        try {
            findMessageById(id);
            messageRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }
}
