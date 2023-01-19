package com.mracover.telegram_bot.service.impl;

import com.mracover.telegram_bot.exception.DatabaseException;
import com.mracover.telegram_bot.exception.feedbackException.NoSuchFeedbackException;
import com.mracover.telegram_bot.model.Feedback;
import com.mracover.telegram_bot.repository.FeedbackRepository;
import com.mracover.telegram_bot.service.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    @Transactional
    public Feedback addFeedback(Feedback feedback) throws DatabaseException{
        try {
            return feedbackRepository.save(feedback);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedbacks() throws DatabaseException, NoSuchFeedbackException {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
        if (feedbacks.isEmpty()) {
            throw new NoSuchFeedbackException("Отзывы не найдены");
        }
        return feedbacks;
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback findFeedbackById(Long id) throws DatabaseException, NoSuchFeedbackException {
        try {
            return feedbackRepository.findById(id).orElseThrow(() ->
                    new NoSuchFeedbackException("Отзыв не найден"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public Feedback updateFeedback(Feedback feedback) throws DatabaseException, NoSuchFeedbackException  {
        try {
            return Optional.of(feedbackRepository.save(feedback)).orElseThrow(() ->
                    new NoSuchFeedbackException("Обновляемый отзыв не найден"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public void deleteFeedbackById(Long id) throws DatabaseException, NoSuchFeedbackException  {
        try {
            findFeedbackById(id);
            feedbackRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }
}
