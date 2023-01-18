package com.mracover.telegram_bot.repository;


import com.mracover.telegram_bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public void deleteUserByTelegramUserId(Long chatId);
    public User findUserByTelegramUserId(Long chatId);
}
