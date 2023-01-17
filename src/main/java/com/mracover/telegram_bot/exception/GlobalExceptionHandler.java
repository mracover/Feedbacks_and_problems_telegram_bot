package com.mracover.telegram_bot.exception;

import com.mracover.telegram_bot.exception.feedbackException.NoSuchFeedbackException;
import com.mracover.telegram_bot.exception.problemException.NoSuchProblemException;
import com.mracover.telegram_bot.exception.userException.NoSuchUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<GlobalExceptionResponse> databaseExceptionHandle(DatabaseException exception) {
        GlobalExceptionResponse data = new GlobalExceptionResponse();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<GlobalExceptionResponse> noSuchUserExceptionHandle(NoSuchUserException exception) {
        GlobalExceptionResponse data = new GlobalExceptionResponse();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchProblemException.class)
    public ResponseEntity<GlobalExceptionResponse> noSuchProblemExceptionHandle(NoSuchProblemException exception) {
        GlobalExceptionResponse data = new GlobalExceptionResponse();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchFeedbackException.class)
    public ResponseEntity<GlobalExceptionResponse> noSuchFeedbackExceptionHandle(NoSuchFeedbackException exception) {
        GlobalExceptionResponse data = new GlobalExceptionResponse();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

//    Общие исключения
    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponse> exceptionHandle(Exception exception) {
        GlobalExceptionResponse data = new GlobalExceptionResponse();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    /*TODO: Подумать на реализацией дополнительных кастомных exception*/
}
