package com.mracover.telegram_bot.service.impl;

import com.mracover.telegram_bot.exception.DatabaseException;
import com.mracover.telegram_bot.exception.userException.NoSuchUserException;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.repository.UserRepository;
import com.mracover.telegram_bot.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User addUser(User user) throws DatabaseException {
        try {
            return userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() throws DatabaseException, NoSuchUserException {
        List<User> users;
        try {
            users = userRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
        if (users.isEmpty()) {
            throw new NoSuchUserException("Пользователи не найдены");
        }
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) throws DatabaseException, NoSuchUserException {
        try {
            return userRepository.findById(id).orElseThrow(() ->
                    new NoSuchUserException("Пользователь не найден"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) throws DatabaseException, NoSuchUserException {
        try {
            return Optional.of(userRepository.save(user)).orElseThrow(() ->
                    new NoSuchUserException("Обновляемый пользователь не найден"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) throws DatabaseException, NoSuchUserException {
        try {
            findUserById(id);
            userRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public void deleteUserByTelegramId(Long userId) {
        try {
            findUserByTelegramId(userId);
            userRepository.deleteUserByTelegramUserId(userId);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByTelegramId(Long userId) throws DatabaseException, NoSuchUserException {
        try {
            return userRepository.findUserByTelegramUserId(userId).orElseThrow(() ->
                    new NoSuchUserException("Пользователь не найден"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

}
