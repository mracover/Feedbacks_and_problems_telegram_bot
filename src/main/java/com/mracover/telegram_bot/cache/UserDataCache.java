package com.mracover.telegram_bot.cache;

import com.mracover.telegram_bot.botapi.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/* in-memory cache
 Сохраняем статут бота для пользователей*/
@Component
public class UserDataCache implements DataCache{
    private Map<Long, BotState> usersBotsStates = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(long userId, BotState botState) {
        usersBotsStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(long userId) {
        BotState botState = usersBotsStates.get(userId);
        if (botState == null) {
            botState = BotState.START;
        }
        return botState;
    }
}
