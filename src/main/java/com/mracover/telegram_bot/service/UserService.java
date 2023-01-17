package com.mracover.telegram_bot.service;


import com.mracover.telegram_bot.model.User;

import java.util.List;

public interface UserService {
    User addUser (User user);
    List<User> getAllUsers();
    User findUserById(Long id);
    User updateUser(User user);
    void deleteUserById(Long id);
    void deleteUserByTelegramId(Long userId);
    User findUserByTelegramId(Long userId);

}
