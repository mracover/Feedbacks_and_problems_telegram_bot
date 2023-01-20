package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.exception.feedbackException.NoSuchFeedbackException;
import com.mracover.telegram_bot.model.Feedback;
import com.mracover.telegram_bot.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/feedbacks")
@Tag(name = "Контроллер отзывов", description = "Методы для работы с отзывами (отсутсвует добавление и обновление отзывов)")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "Возращает все отзывы от пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отзывы найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Feedback.class))
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Нет ни одного отзыва в базе данных",
                    content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NoSuchFeedbackException.class)
                            )
            )
    })
    @GetMapping("/")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @Operation(summary = "Возращает отзыв по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отзыв найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Feedback.class)
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Отзыв не найден",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchFeedbackException.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable("id") long id) {
        Feedback feedback = feedbackService.findFeedbackById(id);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    @Operation(summary = "Удаляет отзыв по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отзыв удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Отзыва с таким id не сущетсвует",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchFeedbackException.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable long id) {
        feedbackService.deleteFeedbackById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
