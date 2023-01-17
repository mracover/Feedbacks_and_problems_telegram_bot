package com.mracover.telegram_bot.cache;

import com.mracover.telegram_bot.botapi.BotState;
import com.mracover.telegram_bot.model.User;


public interface DataCache {
    void setUsersCurrentBotState(long userId, BotState botState);
    BotState getUsersCurrentBotState(long userId);
}
