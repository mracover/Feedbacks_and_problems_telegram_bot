package com.mracover.telegram_bot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Выбирает обработчика сообщения для определенного состояния

@Component
public class BotStateContext {

    private final Map<BotState, InputMessageHandler> messageHandlerMap = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> messageHandlerMap.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessage = findMessageHandler(currentState);
        return currentMessage.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (problemOrFeedback(currentState)) {
            return messageHandlerMap.get(BotState.PROBLEM);
        } else {
            return messageHandlerMap.get(BotState.FEEDBACK);
        }
    }

    private boolean problemOrFeedback(BotState currentState) {
        return switch (currentState) {
            case PROBLEM, ASK_PROBLEM_NAME, ASK_PROBLEM_EMAIL, ASK_PROBLEM_PRODUCT, ASK_PROBLEM_MESSAGE, ASK_IMAGES_PROBLEM, PROBLEM_END ->
                    true;
            default -> false;
        };
    }
}
