package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.model.Feedback;
import com.mracover.telegram_bot.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable("id") long id) {
        Feedback feedback = feedbackService.findFeedbackById(id);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable long id) {
        feedbackService.deleteFeedbackById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
