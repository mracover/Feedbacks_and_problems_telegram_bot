package com.mracover.telegram_bot.exception.userException;

public class NoSuchUserException extends RuntimeException  {
    public NoSuchUserException(String message) {
        super(message);
    }
}
