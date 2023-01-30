package com.mracover.telegram_bot.service.impl;

import com.mracover.telegram_bot.exception.DatabaseException;
import com.mracover.telegram_bot.exception.userException.NoSuchUserException;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.repository.UserRepository;
import com.mracover.telegram_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
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
            log.error(ex.getMessage());
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
        } catch (NoSuchUserException ex) {
            throw new NoSuchUserException(ex.getMessage());
        }
        catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) throws DatabaseException, NoSuchUserException {
        try {
            User upUser = userRepository.save(user);
            if (upUser == null) {
                throw new NoSuchUserException("Обновляемый пользователь не найден");
            }
            return upUser;
        } catch (NoSuchUserException ex) {
            throw new NoSuchUserException(ex.getMessage());
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
    public User findUserByTelegramId(Long userId)  {
        return userRepository.findUserByTelegramUserId(userId);
    }

}
