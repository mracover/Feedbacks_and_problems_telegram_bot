package com.mracover.telegram_bot.service;

import com.mracover.telegram_bot.exception.feedbackException.NoSuchFeedbackException;
import com.mracover.telegram_bot.model.Feedback;
import com.mracover.telegram_bot.repository.FeedbackRepository;
import com.mracover.telegram_bot.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {
    private final static Feedback feedback = new Feedback();
    private FeedbackRepository feedbackRepository;
    private FeedbackService feedbackService;


    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        this.feedbackRepository = Mockito.mock(FeedbackRepository.class);
        this.feedbackService = new FeedbackServiceImpl(feedbackRepository);
    }

    @Test
    void shouldAddFeedback() {
        Mockito.doReturn(feedback).when(feedbackRepository).save(feedback);
        var answer = feedbackService.addFeedback(feedback);
        assertNotNull(answer);
    }

    @Test
    void shouldGetAllFeedbacks() {
        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback);
        Mockito.doReturn(feedbackList).when(feedbackRepository).findAll();
        var answer = feedbackService.getAllFeedbacks();
        assertFalse(answer.isEmpty());
    }

    @Test
    void shouldThrowNoSuchFeedbackExceptionWhenGetAllFeedbacks() {
        List<Feedback> feedbackList = new ArrayList<>();
        Mockito.doReturn(feedbackList).when(feedbackRepository).findAll();
        assertThrows(NoSuchFeedbackException.class, () ->
                feedbackService.getAllFeedbacks());
    }

    @Test
    void shouldFindFeedbackById() {
        Mockito.doReturn(Optional.of(new Feedback())).when(feedbackRepository).findById(Mockito.anyLong());
        var answer = feedbackService.findFeedbackById(Mockito.anyLong());
        assertNotNull(answer);
    }

    @Test
    void shouldThrowNoSuchFeedbackExceptionWhenFindFeedbackById() {
        Mockito.doReturn(Optional.empty()).when(feedbackRepository).findById(Mockito.anyLong());
        assertThrows(NoSuchFeedbackException.class, () ->
                feedbackService.findFeedbackById(Mockito.anyLong()));
    }

    @Test
    void shouldUpdateFeedback() {
        Mockito.doReturn(new Feedback()).when(feedbackRepository).save(feedback);
        var answer = feedbackService.updateFeedback(feedback);
        assertNotNull(answer);
    }

    @Test
    void shouldThrowNoSuchFeedbackExceptionWhenUpdateFeedback() {
        Mockito.doReturn(null).when(feedbackRepository).save(feedback);
        assertThrows(NoSuchFeedbackException.class, () ->
                feedbackService.updateFeedback(feedback));
    }

    @Test
    void shouldDeleteFeedbackById() {
        Mockito.doReturn(Optional.of(new Feedback())).when(feedbackRepository).findById(Mockito.anyLong());
        Mockito.doNothing().when(feedbackRepository).deleteById(Mockito.anyLong());
        assertDoesNotThrow(() -> feedbackService.deleteFeedbackById(Mockito.anyLong()));
    }
}