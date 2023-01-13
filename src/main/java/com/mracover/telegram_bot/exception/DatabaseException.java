package com.mracover.telegram_bot.exception;


public class DatabaseException extends RuntimeException{
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
