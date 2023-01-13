package com.mracover.telegram_bot.exception;


public class DatabaseException extends RuntimeException{
    private static final String MESSAGE = "Ошибка соединения с базой данных";
    public DatabaseException() {
        super(MESSAGE);
    }
}
