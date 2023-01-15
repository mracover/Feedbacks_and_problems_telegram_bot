package com.mracover.telegram_bot.exception.feedbackException;

public class NoSuchFeedbackException extends RuntimeException{
    public NoSuchFeedbackException(String message) {
        super(message);
    }
}
