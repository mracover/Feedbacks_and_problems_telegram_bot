package com.mracover.telegram_bot.service;

import com.mracover.telegram_bot.model.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback addFeedback (Feedback feedback);
    List<Feedback> getAllFeedbacks();
    Feedback findFeedbackById(Long id);
    Feedback updateFeedback(Feedback feedback);
    void deleteFeedbackById(Long id);
}
