package com.mracover.telegram_bot.exception.messageException;

public class NoSuchMessageException extends RuntimeException{
    public NoSuchMessageException(String message) {
        super(message);
    }
}
