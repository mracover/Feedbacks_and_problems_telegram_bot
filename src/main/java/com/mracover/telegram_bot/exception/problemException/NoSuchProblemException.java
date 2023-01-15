package com.mracover.telegram_bot.exception.problemException;

public class NoSuchProblemException extends RuntimeException{
    public NoSuchProblemException(String message) {
        super(message);
    }
}
