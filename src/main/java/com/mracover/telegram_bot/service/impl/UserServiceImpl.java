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
    public User addUser(User user) {
        try {
            return userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        List<User> users;
        try {
            users = userRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (users.isEmpty()) {
            throw new NoSuchUserException("Пользователи не найдены");
        }
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(String id) {
        Optional<User> user;
        try {
            user = userRepository.findById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (user.isEmpty()) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        return user.get();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        Optional<User> updateUser;
        try {
            updateUser = Optional.of(userRepository.save(user));
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (updateUser.isEmpty()) {
            throw new NoSuchUserException("Обновляемый пользователь не найден");
        }
        return updateUser.get();
    }

    @Override
    @Transactional
    public void deleteProductById(String id) {
        Optional<User> user;
        try {
            user = userRepository.findById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
        if (user.isEmpty()) {
            throw new NoSuchUserException("Удаляемый пользователь не найден");
        }
        try {
            userRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException("Ошибка соединения с базой данных", ex.getCause());
        }
    }
}
